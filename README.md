# Why Jooby?

- Fast, light, easy to learn
- I've been using Spring for almost my wokring time with Java/Kotlin. For large scale enterprise projects, Spring Stacks
  are undoubtedly the way to go, but for minimal projects, Jooby is a good choice.

## Looking for Kotlin version

- [Checkout](https://github.com/jonaskahn/kooby-api-template)

## What's included?

- [x] Support Default JWT
- [x] Support Role Access Layer
- [x] Hibernate, Flyway support by default
- [x] Add custom JPAQueryExecutor for the better querying
- [x] Add Jedis support instead of Lettuce
- [x] Using MapStruct for Object Mapper
- [x] Using Guice as Dependency Injection Framework
- [x] Multiple language support
- [x] Default admin user: `admin@localhost/admin`

## Default API

1. Create users

```shell
curl --location 'http://localhost:8080/api/auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "test",
    "email": "test@localhost",
    "password": "test"
}'
```

2. Generate token

```shell
curl --location 'http://localhost:8080/api/auth/generate-token' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "test@localhost",
    "password": "test"
}'
```

3. Get User Info

```shell
# With JPA Query
curl --location 'http://localhost:8080/api/secure/user/info' \
--header 'Accept-Language: vi'
--header 'Authorization: ••••••'
```

```shell
# With JPAQueryExecutor
curl --location 'http://localhost:8080/api/secure/user/info-with-executor' \
--header 'Accept-Language: vi'
--header 'Authorization: ••••••'
```

4. Test Role

```shell
curl --location 'http://localhost:8080/api/secure/test/admin-role' \
--header 'Authorization: ••••••'
```

5. Logout

```shell
curl --location --request DELETE 'http://localhost:8080/api/auth/secure/logout' \
--header 'Authorization: ••••••'
```

## HOW

### Implement JWT

- Using [Pac4j](https://jooby.io/modules/pac4j/) Module as Security Layer

```java

        install(new Pac4jModule().client(
                        "/api/secure/*",
                        conf -> new HeaderClient(
                                "Authorization",
                                "Bearer ",
                                new AdvancedJwtAuthenticator(
                                        require(JedisPooled.class),
                                        new SecretSignatureConfiguration(conf.getString("jwt.salt")
                                        )
                                )
                        )
                )
        );


```

1. Using `HeaderClient` to tell Jooby read `Bearer` token from header
2. By default Jooby use the `JwtAuthenticator` from Pac4j, the problems are:
    - Token is completed stateless
    - What if user is lock/inactivated/deleted -> token may still valid by the `exp` -> user still can access to system
    - There is no truly `logout`

So, I solved these problems by store `jid` of JWT in Redis, after `validate` raw token, before `createProfile` I made a
simple check to ensure the `jid` exists in `redis`. If no, token is invalid

See [AdvancedJwtAuthenticator.kt](src/main/java/io/github/jonaskahn/middlewares/jwt/AdvancedJwtAuthenticator.java)

```java
public class AdvancedJwtAuthenticator extends JwtAuthenticator {
    private final JedisPooled redis;

    public AdvancedJwtAuthenticator(JedisPooled redis, SignatureConfiguration signatureConfiguration) {
        super(signatureConfiguration);
        this.redis = redis;
    }

    @Override
    protected void createJwtProfile(TokenCredentials credentials, JWT jwt, WebContext context, SessionStore sessionStore) throws ParseException {
        var jwtId = jwt.getJWTClaimsSet().getJWTID();
        var uid = jwt.getJWTClaimsSet().getClaims().get(Jwt.Attribute.UID).toString();
        if (!redis.exists(RedisNameSpace.getUserTokenExpirationKey(uid, jwtId))) {
            throw new AuthorizationException();
        }
        super.createJwtProfile(credentials, jwt, context, sessionStore);
    }
}

```

3. For now, when you want `logout`, just delete the related `jid` in `redis`.

### [Role Access Verifier](src/main/java/io/github/jonaskahn/middlewares/role/AccessVerifier.java)

```java

@AllArgsConstructor(access = AccessLevel.PUBLIC, onConstructor = @__({@Inject}))
public class AccessVerifierImpl implements AccessVerifier {
    private Context context;

    @Override
    public boolean hasAnyRoles(String... roles) {
        var rolesAsList = Arrays.asList(roles);
        return Optional.ofNullable(context.getUser())
                .map(UserProfile.class::cast)
                .map(UserProfile::getRoles)
                .stream()
                .flatMap(Set::stream)
                .anyMatch(rolesAsList::contains);
    }

    @Override
    public void requireAnyRoles(String... roles) {
        if (!hasAnyRoles(roles)) {
            throw new ForbiddenException();
        }
    }
}

```

- `hasRole` or `hasAnyRoles` will check and return `true`/`false`, while `requireRole` and `requireAnyRoles` will
  explicitly throw exception if you do not have access.

### [JpaQueryExecutor](src/main/java/io/github/jonaskahn/assistant/query/JpaQueryExecutor.java)

- **Problem**: Sometimes we want to retrieve data from database via native query, but we do not want manually map
  field's value from result to pojo class.
- To solve this problem we have so many ways. With the usage of Hibernate, I create JPA Query Executor to parse sql
  result to object via Jackson

```java
public Optional<UserDto> findCustomActivatedUserByPreferredUsername(Long preferredUsername) {
    var query =
            entityManager.createNativeQuery("select * from users where preferred_username = :preferredUsername and status = :status");
    try {
        var result = JpaQueryExecutor
                .builder(UserDto.class)
                .with(query, Map.of(
                                "preferredUsername", preferredUsername,
                                "status", Status.Code.ACTIVATED
                        )
                )
                .getSingleResult();
        return Optional.of(result);
    } catch (Exception e) {
        log.warn("Could not find the users with given preferredUsername", e);
        return Optional.empty();
    }
}
```

> So, we directly map database field's value to Pojo object via Jackson, if Pojo class does not have correct field name,
> please using @JsonAlias (Like database field `full_name`, pojo class `fullName`)

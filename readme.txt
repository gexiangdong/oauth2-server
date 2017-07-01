0、create database in postgresql, "oauth2server", and execute schema-and-sample-data.sql in the db.
1. run
#mvn spring-boot:run

2. test
2.0 --- (需要oauth_client_details表增加对应的记录)
#curl -v -u my-client-with-secret:secret  -d "grant_type=client_credentials" http://localhost:8081/oauthserver/oauth/token?grant_type=client_credentials

my-client-with-secret & secret is stored in table oauth_client_details

2.1
### redirect to this url to get authorization code
http://localhost:8081/oauthserver/oauth/authorize?response_type=code&client_id=rs1&redirect_url=http%3a%2f%2f127.0.0.1%3Fkey%3Drs1&scope=read

2.2
### --- code for accesstoken ------
#curl rs1:@localhost:8081/oauthserver/oauth/token  -d grant_type=authorization_code -d client_id=rs1  -d redirect_uri=http://127.0.0.1%3Fkey%3Drs1 -d code=GlO7wT
##---return value ---
{
    "access_token":"f3885d37-ece0-481e-88b3-f00c27ed8a34",
    "token_type":"bearer",
    "expires_in":43199,
    "scope":"read trust",
    "phone":null,
    "headImage":null,
    "name":null,
    "userId":0,
    "email":null,
    "authorities":[
        {
            "authority":"all"
        },
        {
            "authority":"everything"
        }
    ]
}

2.3
###------ CHECK ACCESSTOKEN -------
#curl -X POST rs1:@localhost:8081/oauthserver/oauth/check_token -d "token=f3885d37-ece0-481e-88b3-f00c27ed8a34"
##--return value ---
{
    "aud":[
        "oauth2-resource"
    ],
    "phone":null,
    "user_name":null,
    "scope":[
        "read",
        "trust"
    ],
    "headImage":null,
    "name":null,
    "exp":1498052466,
    "userId":0,
    "authorities":[
        {
            "authority":"all"
        },
        {
            "authority":"everything"
        }
    ],
    "email":null,
    "client_id":"rs1"
}
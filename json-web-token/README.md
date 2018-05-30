README
===========================
使用JWT的oauth2 server例子，含自定义的jwt内容【自定义内容通过AuthoritiesTokenEnhancer实现】

使用命令
```Bash
mvn spring-boot:run
```
可以运行项目。配合[security-mvc-jwt](../../../security-mvc-jwt)一起来查看运行示例。

## 运行前的准备工作

### 数据库
使用postgresql数据库，数据库名为：oauth2server。建表和示例数据参照[schema-and-sample-data.sql](schema-and-sample-data.sql)

### 生成public key和private key
使用如下命令生成key
```Bash
keytool -genkeypair -alias thejwttokenkey  -keyalg RSA -keypass hellojwttoken -keystore oauthserver.jks -storepass hellojwttoken
```

使用如下命令生成public.txt                 
```Bash
keytool -list -rfc --keystore oauthserver.jks | openssl x509 -inform pem -pubkey
```


### 测试

获取token测试：

```Bash
curl http://rs1@localhost:8009/oauth/token -d grant_type=password -d username=admin -d password=admpwd
```

或

```Bash
curl http://localhost:8009/oauth/token -d grant_type=password -d username=admin -d password=admpwd -d client_id=rs1 -d client_secret=
```


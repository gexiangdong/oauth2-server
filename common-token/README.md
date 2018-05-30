README
===================

Spring oauth2 server示例。

### 环境

使用postgresql数据库，数据库名为：oauth2server。建表和示例数据参照[schema-and-sample-data.sql](schema-and-sample-data.sql)。

### 测试
本例中数据库内有2个oauth_client, rs1和rs2

| oauth_client | password | grant_type |
|:----:|:----:|:----:|
| rs1 | 无 | password |
| rs2 | secret | authorization_code |

#### password类型

获取token测试：

```Bash
curl http://rs1@localhost:8008/oauth/token -d grant_type=password -d username=admin -d password=admpwd
```

或

```Bash
curl http://localhost:8008/oauth/token -d grant_type=password -d username=admin -d password=admpwd -d client_id=rs1 -d client_secret=
```

#### authorization_code类型
打开浏览器访问：
http://localhost:8008/oauth/authorize?response_type=code&client_id=rs2&amp;redirect_url=http%3a%2f%2f127.0.0.1%3a8080

会出现输入用户名和密码的登录页面，用户名是admin，密码是admpwd，输入完成后会调整到类似 http://127.0.0.1:8080/?code=6Tq7Jf 的页面（后面的code每次不同，这个code就是authorization_code，跳转到的页面应该获取这个code，并且访问http://127.0.0.1:8008/oauth/token用code换取token。用curl测试code换token如下：

```Bash
curl rs2:secret@localhost:8008/oauth/token -d grant_type=authorization_code  -d code=6Tq7Jf
```
6Tq7Jf需要替换成上步得到code



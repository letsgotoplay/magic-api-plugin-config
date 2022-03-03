# magic-api-plugin-config

# 介绍
- 用于[magic-api](https://gitee.com/ssssssss-team/magic-api) 的配置文件插件
- 支持在线添加并热加载配置文件
- 对env对象扩展,支持代码中动态插入配置


# 部署步骤

## maven编译
```cmd
 mvn clean install
```
## pom引入
```xml
<!-- 配置文件 -->
<dependency>
    <groupId>cn.luow</groupId>
    <artifactId>magic-api-plugin-config</artifactId>
    <version>2.0.0-beta.2</version>
</dependency>
```
# 使用说明

### 给需要热加载配置的类添加注解@RefreshScope
```java
@Service
@RefreshScope
public class TestService {
  @Value("${test}")
  private String test;

  @Value("${testArr}")
  private List<String> testArr;

  public String getTest() {
    return test;
  }

  public List<String> getTestArr() {
    return testArr;
  }
}
```
### 创建一个配置文件，添加相关配置，勾上启用后保存
![](https://luow.fun:5305/uploads/big/ffa3b62b31f2bbc32d0633370ec3e670.png)
### 测试配置文件是否生效
![](https://luow.fun:5305/uploads/big/3a295295a187c0a819621ef9f24ee18d.png)

#脚本中动态配置
```javascript
env.set("test","testSet");

var yml = """
test : testYmal
test1:
    test2: 123
test3:
    test2:
        - 12
        - 123
        - 12314
"""
env.setYaml(yml,"testYaml");
```

- 配置文件刷新需要时间，并不是实时的
- 配置文件拥有覆盖性，后加载的覆盖之前的
- 后加载的配置禁用之后，之前被覆盖的配置依旧会生效




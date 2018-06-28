# thrift-zk-rpc

## 1.zookeeper配置
### config path

注册zookeeper配置默认径路<br>
`DEFAULT_CFG_PATH = "/root/ZK_Server_Config/"`

在properties文件自定义配置路径<br>
`zk.cfg.path=/services/zk_config/`

### zk_server.xml config context

    <?xml version="1.0" encoding="utf-8" standalone="yes"?>
    
    <!DOCTYPE zk_server_set [ 
    <!ELEMENT zk_server_set (zk_server*)>
    <!ELEMENT zk_server (ip, port)>
    <!ELEMENT ip (#PCDATA)>
    <!ELEMENT port (#PCDATA)>
    ]>

    <zk_server_set>
      <zk_server>
	    <!--dev-->
	    <!--<ip>192.168.28.5</ip>-->
	    <!--<ip>192.168.28.6</ip>-->
	    <!--<ip>192.168.28.7</ip>-->

	    <!--beta-->
	    <!--<ip>10.252.137.168</ip>-->

	    <!--alpha-->
	    <!--<ip>121.40.57.205</ip>-->

	    <ip>192.168.28.7</ip>
	    <port>2181</port>
      </zk_server>
    </zk_server_set>
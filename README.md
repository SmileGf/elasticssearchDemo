一. 安装：
  1. 下载ES:	 
    wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.5.3.tar.gz

  2. 解压并移动到local目录下
    $ tar -zxvf elasticsearch-5.5.3.tar.gz
    $ mv elasticsearch-5.5.3 /usr/local/elasticsearch

  3. 修改config 目录下的elasticsearch.yml 文件
    vim elasticsearch.yml
    // 去掉行开头的 # 并重命名集群名，这里命名为 compass
    cluster.name: compass
    // 去掉行开头的 # 并重命名节点名，这里命名为 node-1
    node.name: node-1

  4. 进入 bin 目录启动 ES 并在后台运行
    $ ./elasticsearch -d

  5. 启动之后测试是否正常
    curl	127.0.0.1:9200

  6. Logstash 
    
    6.1 下载安装包
      $ wget https://artifacts.elastic.co/downloads/logstash/logstash-5.5.3.tar.gz

    6.2 解压并移动到local目录下
      $ tar -zxvf logstash-5.5.3.tar.gz
      $ mv logstash-5.5.3 /usr/local/logstash

   7. kibana
     7.1 下载安装包
      $ wget https://artifacts.elastic.co/downloads/kibana/kibana-5.5.3-linux-x86_64.tar.gz

      7.2 解压并移动到local目录下
        $ tar -zxvf kibana-5.5.3-linux-x86_64.tar.gz
        $ mv kibana-5.5.3-linux-x86_64 /usr/local/kibana

      7.3 修改config 目录下的kibana.yml 文件
        // 去掉当前行开头的 #
        server.port: 5601
        // 去掉当前行开头的#并将localhost修改为具体IP
        server.host: "192.168.1.191"
        // 去掉当前行开头的#并将localhost修改为具体IP
        elasticsearch.url: "http://192.168.1.191:9200"

      7.4 启动Kibana，浏览器访问 http://192.168.1.191：5601
        $ ./kibana

二. 日常问题
  1. 不能使用管理员运行，此时需要切换到普通账户或者新建ES账号
    1.1	新建用户组
      $ groupadd elsearch
    
    1.2	新建用户并指定用户组
      $ useradd elsearch -g elsearch -p elasticsearch
      $ useradd -g elasticsearch elasticsearch

    1.3	修改ES目录所属者
      $ chown -R elasticsearch:elasticsearch elasticsearch-6.3.0
    
    1.4 切换用户再次启动
      $ su elasticsearch
      $ ./elasticsearch -d 

    备注： 添加用户组 elsearch  
          添加用户 elsearch 密码为 elasticsearch 到用户组 elsearch
          将elsearch安装目录授权给 用户组：用户  即 elsearch：elsearch

  2. 只能使用127.0.0.1 或者 localhost访问，使用ip地址无法访问。
    2.1 修改 elasticsearch.yml 中的 network.host
      $ network.host: 0.0.0.0
    
    2.2 重启ES出现如下报错，按下面的步骤解决
      ERROR: [3] bootstrap checks failed
      [1]: max file descriptors [4096] for elasticsearch process is too  low,increase to at least [65536]
      [2]: max number of threads [3818] for user [elasticsearch] is too low,increase to at least [4096]
      [3]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
  
     【1】 max file descriptors [4096] for elasticsearch process is too low, increase to at least [65536]
        每个进程最大同时打开文件数太小
        修改 /etc/security/limits.conf 文件，增加如下配置，用户退出后重新登录生效
          * soft nofile 65536
          * hard nofile 65536
     
     【2】max number of threads [3818] for user [es] is too low, increase to at least [4096]
        最大线程个数太低
        同上修改 /etc/security/limits.conf 文件，增加如下配置，用户退出后重新登录生效
        * soft nproc 4096
        * hard nproc 4096
  
     【3】max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
         一个进程能拥有的最多的内存区域
         修改 /etc/sysctl.conf 文件，增加如下配置，执行命令「 sysctl -p 」生效
         vm.max_map_count=262144
  
    3. 切换到elasticsearch 用户 并重启，curl 测试

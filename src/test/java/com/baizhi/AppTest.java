package com.baizhi;


import com.baizhi.rpc.HostAndPort;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppTest{
    private ZkClient zkClient;

    @Before
    public void befor(){
        zkClient=new ZkClient("192.168.152.128:2181");
    }

    @Test
    //添加节点
    /*
    * EPHEMERAL:临时节点
    *EPHEMERAL_SEQUENTIAL:临时序列
    * PERSISTENT:永久节点
    * PERSISTENT_SEQUENTIAL:永久序列
    * */


    public void TestCreate(){
        String s = zkClient.create("/zpark", "闺中少妇不知愁", CreateMode.PERSISTENT);
        String s2 = zkClient.create("/zpark/zpark1", "春日凝装上翠楼", CreateMode.PERSISTENT);
       // String s3 = zkClient.create("/zpark/zpark2", "忽见陌头杨柳色", CreateMode.PERSISTENT);
        String s4 = zkClient.create("/zpark/zpark1/zpark2", "悔教夫婿觅封侯", CreateMode.PERSISTENT);
        //System.out.println(s);
    }

    @Test
    //删除节点
    public void TestDelete(){
        //递归删除非空节点
        boolean b = zkClient.deleteRecursive("/services/8989");
        //删除空节点
        boolean b1 = zkClient.delete("/zoo");
        System.out.println(b+"   "+b1);
    }
    @Test
    //修改节点
    public void TestUpdate(){
        zkClient.writeData("/zpark/zpark1","春日凝装上jkjk翠楼");

    }
    @Test
    //查看节点
    public void TestShow(){
        Object o = zkClient.readData("/zpark");
        System.out.println(o);

    }
    @Test
    //查看节点是否存在
    public void TestIsExists(){
        boolean exists = zkClient.exists("/zpark");
        System.out.println("在否 ？"+exists);
    }


    @Test
    //查看子节点
    public void TestShowChild(){
        List<String> children = zkClient.getChildren("/services");
        for (String child : children) {
            System.out.println(child);
        }
    }

    @Test
    //监听节点数据的变化
    public void watchZkNodeDate() throws IOException {
        zkClient.subscribeDataChanges("/zpark", new IZkDataListener(){

            @Override
            //数据变化
            public void handleDataChange(String s, Object o) throws Exception {
                /*
                 * @Param s 监听的节点名
                 * @Param 0 改变的数据
                 *
                 *注意：只能监听该节点本身，子节点的变化无法监听
                 * */
                System.out.println(s);
                System.out.println("=========================");
                System.out.println(o);
            }

            @Override
            //数据删除
            public void handleDataDeleted(String s) throws Exception {
                /*
                *
                * @Param s 监听的节点名
                *
                * */
                System.out.println(s);
            }
        });
        System.in.read();

    }

    @Test
    //监听节点的变化
    public void watchZkNode() throws IOException {
        zkClient.subscribeChildChanges("/zpark", new IZkChildListener(){

            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
               /*
               * @Param s 坚挺的节点
               * @Param list 子节点
               *
               * 注意：只监听子节点和本身的创建和销毁
               *        孙子节点，以及本身数据、子节点数据变化不监听
               *
               * */



                System.out.println(s);
                System.out.println("================");
                for (String s1 : list) {
                    System.out.println(s1);
                }
            }
        });
        System.in.read();

    }


    @Test
    //查看节点
    public void Test1(){
        List<String> children = zkClient.getChildren("/services");
        ArrayList<HostAndPort> hostAndPorts = new ArrayList<>();
        for (String child : children) {
            HostAndPort hostAndPort = (HostAndPort) zkClient.readData("/services/"+child);
            hostAndPorts.add(hostAndPort);
        }
        System.out.println(hostAndPorts.get(0).getHost()+"\n"+hostAndPorts.get(0).getPort());

    }





    @After
    public void after(){
        zkClient.close();
    }

}

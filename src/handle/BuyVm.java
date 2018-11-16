package handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.User;
import org.apache.log4j.Logger;
import pojo.Vm;
import pojo.VmPay;
import redis.clients.jedis.Jedis;
import util.Utils;

public class BuyVm {
    private static Logger log=Logger.getLogger(BuyVm.class);
    Jedis jedis = new Jedis("127.0.0.1",6379);
    User user=new User();

    //计算云主机的价格
    public int calculatePrice(Vm vm){
        log.info("价格为100学时");
        return 100;

    }

    //查询用户的剩余学时
    public int getMoney(String pid){

        int account;
        if (jedis.hexists(pid,"m")){
            account=Integer.parseInt(jedis.hget(pid,"m"));
            log.info("redis 已记录该用户学时-------->pid："+pid+"   account:"+account);
        }
        else {
            account=user.getAccount(pid);
            jedis.hset(pid,"m",String.valueOf(account));
            log.info("已从db中查到余额并存入redis-------->pid:"+pid+"  account:"+account);
        }
        return account;
    }

    //购买云主机
    public String buy(Vm vm){
        String pid= vm.getPid();
        int account = getMoney(pid);
        int price=calculatePrice(vm);
        int currentAccount;
        String json="";
        VmPay vmPay=new VmPay();
        Utils utils=new Utils();
        if (account>=price){
            log.info("用户当前可用余额大于价格---------->pid:"+pid);
            currentAccount=account-price;
            jedis.hset(pid,"account",String.valueOf(currentAccount));
            user.updateAccount(currentAccount,pid);
            vmPay.setState(true);
            log.info("vm购买成功------------>pid:"+pid+"  currentAccount:"+currentAccount);
            String payDate=utils.getDate();
            if ((vm.getType()).equals("vm1")){
                user.insertMessage(pid,"1","1","1",payDate);
                log.info(pid+"用户购买的vm信息已存");
            }else {
                user.insertMessage(pid,vm.getCpu(),vm.getMem(),vm.getDisk(),payDate);
                log.info(pid+"用户购买的vm信息已存");
            }
            json=JSON.toJSONString(vmPay);
            return  json;
        }
        else {
            log.info("用户购买vm失败-------->pid："+pid);
            vmPay.setState(false);
            json=JSON.toJSONString(vmPay);
            return  json;
        }
    }

    //接受前端的请求，并实例化为vm对象
    public Vm getMessage(String json){
//        Vm vm = JSON.parseObject(json,Vm.class);

        Vm vm=new Vm();
        JSONObject object = JSON.parseObject(json);
        vm.setPid((String) object.get("pid"));
        vm.setType((String) object.get("type"));
        vm.setCpu((String) object.get("cpu"));
        vm.setMem((String) object.get("mem"));
        vm.setDisk((String) object.get("disk"));
        return vm;
    }


    public static void main(String[] args) {
        String json="{\"pid\":\"111\",\"type\":\"vm1\",\"cpu\":\"2\",\"mem\":\"2g\",\"disk\":\"20g\"}";
        BuyVm buyVm =new BuyVm();
        Vm vm=buyVm.getMessage(json);
        System.out.println(vm.getCpu()+"  "+vm.getDisk()+"  "+vm.getMem()+"  "+vm.getType()+"  "+vm.getPid());


        System.out.println();
        System.out.println();
        VmPay vmPay=new VmPay();
        vmPay.setState(true);
        String jsons=JSON.toJSONString(vmPay);
        System.out.println(jsons);
    }
}

package com.github.tyranitarx.Kyaru.Listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.tyranitarx.Kyaru.Utils.HttpUtil;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.utils.MiraiLogger;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.function.Consumer;

/**
 * @author tyranitar
 * @email tyranitarx@163.com
 * @date 2020-07-23 18:23
 */
public class TotalListener implements Consumer<GroupMessageEvent> {

    public static MiraiLogger log = null;

    public static final String[] defaultAt={
            "叫凯露有什么事呢",
            "凯露爱你哦~",
            "你可以让凯露夸夸你或者别人，输入'夸我'，或者'夸@一位群友哦~'"
    };

    @Override
    public void accept(GroupMessageEvent event) {
        log = event.getBot().getLogger();
        Group group = event.getGroup();
        String message = event.getMessage().toString();
        long groupId = group.getId();
        long qq = event.getSender().getId();
        log.info(event.getMessage()+"");
        dealMessage(group, groupId, qq, message);
    }

    private void dealMessage(Group api, long groupId, long qq, String message) {
        Image image=null;
        Image image2=null;
        Image image3=null;
        try {
            URL url=new URL("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595512154849&di=721e2e1848929cba69a7f68a0631c811&imgtype=0&src=http%3A%2F%2Ft8.baidu.com%2Fit%2Fu%3D572904047%2C4091638234%26fm%3D193");
            URL url1=new URL("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595517110529&di=757cff356016263c36ba45bc24a8a31e&imgtype=0&src=http%3A%2F%2Fimg.nga.178.com%2Fattachments%2Fmon_201909%2F18%2F-9lddQ5-bhw6ZeT1kSe8-e8.gif");
            URL url2 =new URL("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595520199484&di=7288fb38602c4e5f606ce8dfec990ad1&imgtype=0&src=http%3A%2F%2Fiacg.cc%2Fwp-content%2Fuploads%2F2020%2F02%2Fcdcf2d35ce37e84c62e5245808f32085.jpg");
            image=api.uploadImage(url);
            image2=api.uploadImage(url1);
            image3=api.uploadImage(url2);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String imageId1 = image.getImageId();
        String imageId2 =image2.getImageId();
        String imageId3 =image3.getImageId();
        String[] imgs= {imageId1,imageId2,imageId3};
        boolean isCMD = false;

        if(message.contains("色图")&&message.contains("mirai:at:3044668489")){
            String reg = "\\[[^\\u4e00-\\u9fa5]*\\]";
            String x =message.replaceAll(reg,"");
            String keword[]=x.split("色图");
            JSONObject object=HttpUtil.getSeTu(keword[0].trim());
            if(object.getString("code").equals("404")){
                api.sendMessage("没有这样的色图呢");
            }else {
            JSONArray datas =(JSONArray) object.get("data");
            JSONObject data =(JSONObject)datas.get(0);
            Image setu =null;
            try {
                URL url=new URL("https://www.pixivdl.net/"+data.getString("url").substring(20));
                setu = api.uploadImage(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            api.sendMessage(MessageUtils.newImage(setu.getImageId()));
            api.sendMessage("神秘连接=>"+data.getString("url"));
            }
        }
        else if(message.contains("xe在直播吗")){
            JSONObject object = HttpUtil.getxueeeeLiveStatus("169837");
            JSONObject data = (JSONObject) object.get("data");
            String status = data.getString("liveStatus");
            if(status.equals("0")) {
                api.sendMessage("没有哦");
                api.sendMessage(MessageUtils.newImage(imageId2));
            }
            else{
                api.sendMessage("xe正在直播"+data.get("title"));
            }
        }
        else if(message.contains("mirai:at:")&&message.contains("夸")&&!message.contains("骂")){
            String numString = message.split("mirai:at:")[1];
            Long atqq = Long.parseLong(numString.split("]")[0]);
            api.sendMessage(new At(
                    api.getOrNull(atqq)).plus(HttpUtil.getChp()));
        }

        else if(message.contains("mirai:at:")&&message.contains("骂")&&!message.contains("夸")){
            String numString = message.split("mirai:at:")[1];
            long atqq = Long.parseLong(numString.split("]")[0]);
            api.sendMessage(new At(
                    api.getOrNull(atqq)).plus(HttpUtil.getDuixian()));
        }

        else if(message.contains("mirai:at:3044668489")){
            Random random=new Random();
            api.sendMessage(defaultAt[random.nextInt(3)]);
            api.sendMessage(MessageUtils.newImage(imgs[random.nextInt(3)]));
        }
    }
}

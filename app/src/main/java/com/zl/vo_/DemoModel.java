package com.zl.vo_;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.zl.vo_.db.UserDao;
import com.zl.vo_.domain.RobotUser;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.utils.PreferenceManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/9.
 */

public class DemoModel {
    protected Context context = null;
    UserDao dao = null;
    protected Map<Key,Object> valueCache = new HashMap<Key,Object>();

    //全局模型的构造
    public DemoModel(Context ctx){
        context = ctx;
        //初始化储存器
        PreferenceManager.init(ctx);

    }


    /***
     * 获取当前时间是否在设定两个时间点之间
     */

    public boolean isInnerSettingsTime(Date currentTime,Date settingStartTime,Date settingEndTime){
        DateFormat fmt = new SimpleDateFormat("HH:mm");
//        try {
//            settingStartTime = fmt.parse(strBeginTime.toString());//将时间转化成相同格式的Date类型
//            strendDate = fmt.parse(strEndTime.toString());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        return false;

    }


    /***
     * 保存联系人[集合]
     * @param contactList
     * @return
     */
    public boolean saveContactList(List<EaseUser> contactList) {
        UserDao dao = new UserDao(context);
        dao.saveContactList(contactList);
        return true;
    }

    /***
     * 获取联系人[集合]
     * @return
     */
    public Map<String, EaseUser> getContactList() {
        UserDao dao = new UserDao(context);
        return dao.getContactList();
    }

    /****
     * 保存一个联系人
     * @param user
     */
    public void saveContact(EaseUser user){
        UserDao dao = new UserDao(context);
        dao.saveContact(user);
    }
    /**
     * 保存当前用户的名称
     * @param username
     */
    public void setCurrentUserName(String username){
        PreferenceManager.getInstance().setCurrentUserName(username);
    }

    //------vo助手---------------------------------------
    public void setVoAvatar(String voAvatar){
        PreferenceManager.getInstance().setVOAvatar(voAvatar);
    }

    public void setVoNick(String voNick){
        PreferenceManager.getInstance().setVOAvatar(voNick);
    }

    public String getVoAvatar(){
        return PreferenceManager.getInstance().getVOAvatar();
    }

    public String getVoNick(){
        return PreferenceManager.getInstance().getVONick();
    }




    //------vo助手---------------------------------------
    /***
     * 获取当前用户的名称
     * @return
     */
    public String getCurrentUsernName(){
        return PreferenceManager.getInstance().getCurrentUsername();
    }

    /***
     * 获取机器人集合[map]
     * @return
     */
    public Map<String, RobotUser> getRobotList(){
        UserDao dao = new UserDao(context);
        return dao.getRobotUser();
    }

    /***
     * 保存机器人集合[boolean]
     * @param robotList
     * @return
     */
    public boolean saveRobotList(List<RobotUser> robotList){
        UserDao dao = new UserDao(context);
        dao.saveRobotUser(robotList);
        return true;
    }

    /****
     * 设置消息是否通知
     * @param paramBoolean
     */
    public void setSettingMsgNotification(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingMsgNotification(paramBoolean);
        valueCache.put(Key.VibrateAndPlayToneOn, paramBoolean);
    }







    /***
     * 获取是否加我为好友时需要验证
     * @return
     */
    public boolean getSettingVerification() {
        Object val = valueCache.get(Key.VibrateAndPlayToneOn);

        if(val == null){
            val = PreferenceManager.getInstance().getSettingVerification();
            valueCache.put(Key.VibrateAndPlayToneOn, val);
        }

        return (Boolean) (val != null?val:true);
    }

    /****
     * 设置加我为好友时需要验证
     * @param paramBoolean
     */
    public void setSettingVerification(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingVerification(paramBoolean);
        valueCache.put(Key.PlayToneOn, paramBoolean);
    }


    /***
     * 获取是否像我推荐通讯录好友
     * @return
     */
    public boolean getSettingRecommedFriend() {
      /*  Object val = valueCache.get(Key.VibrateAndPlayToneOn);

        if(val == null){
            val = PreferenceManager.getInstance().getSettingRecommedFriend();
            valueCache.put(Key.VibrateAndPlayToneOn, val);
        }*/

        return PreferenceManager.getInstance().getSettingRecommedFriend();
    }

    /****
     * 设置加像我推荐通讯录好友
     * @param paramBoolean
     */
    public void setSettingRecommedFriend(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingRecommedFriend(paramBoolean);
        valueCache.put(Key.PlayToneOn, paramBoolean);
    }












    /***
     * 获取消息是否通知
     * @return
     */
    public boolean getSettingMsgNotification() {
        Object val = valueCache.get(Key.VibrateAndPlayToneOn);

        if(val == null){
            val = PreferenceManager.getInstance().getSettingMsgNotification();
            valueCache.put(Key.VibrateAndPlayToneOn, val);
        }

        return (Boolean) (val != null?val:true);
    }

    /****
     * 设置消息是否有声音
     * @param paramBoolean
     */
    public void setSettingMsgSound(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingMsgSound(paramBoolean);
        valueCache.put(Key.PlayToneOn, paramBoolean);
    }


    /****
     * 获取消息是否设置显示详情
     * @return
     */
    public boolean getSettingMsgDetails() {
//        Object val = valueCache.get(Key.PlayToneOn);
//
//        if(val == null){
//            val = PreferenceManager.getInstance().getSettingMsgDetails();
//            valueCache.put(Key.PlayToneOn, val);
//        }
        return PreferenceManager.getInstance().getSettingMsgDetails();
        //return (Boolean) (val != null?val:true);
    }






    /****
     * 设置消息消息显示详情
     * @param paramBoolean
     */
    public void setSettingMsgDetails(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingMsgDetails(paramBoolean);
        valueCache.put(Key.PlayToneOn, paramBoolean);
    }





    /****
     * 获取消息是否设置消息[boolean]
     * @return
     */
    public boolean getSettingMsgSound() {
        Object val = valueCache.get(Key.PlayToneOn);

        if(val == null){
            val = PreferenceManager.getInstance().getSettingMsgSound();
            valueCache.put(Key.PlayToneOn, val);
        }

        //return (Boolean) (val != null?val:true);
        return PreferenceManager.getInstance().getSettingMsgSound();
    }

    /****
     * 设置消息是否震动
     * @param paramBoolean
     */
    public void setSettingMsgVibrate(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingMsgVibrate(paramBoolean);
        valueCache.put(Key.VibrateOn, paramBoolean);
    }

    /****
     * 获取是否设置消息震动[boolean]
     * @return
     */
    public boolean getSettingMsgVibrate() {
        Object val = valueCache.get(Key.VibrateOn);

        if(val == null){
            val = PreferenceManager.getInstance().getSettingMsgVibrate();
            valueCache.put(Key.VibrateOn, val);
        }

        return (Boolean) (val != null?val:true);
    }







    /****
     * 设置消息提示音
     * @param paramBoolean
     */
    public void setSettingMsgSpeaker(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingMsgSpeaker(paramBoolean);
        valueCache.put(Key.SpakerOn, paramBoolean);
    }

    /****
     * 获取是否设置了消息的提示音
     * @return
     */
    public boolean getSettingMsgSpeaker() {
       /* Object val = valueCache.get(Key.SpakerOn);
        if(val == null){
            val = PreferenceManager.getInstance().getSettingMsgSpeaker();
            valueCache.put(Key.SpakerOn, val);

        }
        return (Boolean) (val != null?val:true);*/
        return PreferenceManager.getInstance().getSettingMsgSpeaker();
    }

    /****
     * 设置组不可用
     * @param groups
     */
    public void setDisabledGroups(List<String> groups){
        if(dao == null){
            dao = new UserDao(context);
        }

        List<String> list = new ArrayList<String>();
        list.addAll(groups);
        for(int i = 0; i < list.size(); i++){
            if(EaseAtMessageHelper.get().getAtMeGroups().contains(list.get(i))){
                list.remove(i);
                i--;
            }
        }

        dao.setDisabledGroups(list);
        valueCache.put(Key.DisabledGroups, list);
    }

    /*****
     * 获取不可用的组
     * @return
     */
    public List<String> getDisabledGroups(){
        Object val = valueCache.get(Key.DisabledGroups);

        if(dao == null){
            dao = new UserDao(context);
        }

        if(val == null){
            val = dao.getDisabledGroups();
            valueCache.put(Key.DisabledGroups, val);
        }

        //noinspection unchecked
        return (List<String>) val;
    }

    /****
     * 设置用户不可用
     * @param ids
     */
    public void setDisabledIds(List<String> ids){
        if(dao == null){
            dao = new UserDao(context);
        }

        dao.setDisabledIds(ids);
        valueCache.put(Key.DisabledIds, ids);
    }

    /****
     * 获取不可用的联系人
     * @return
     */
    public List<String> getDisabledIds(){
        Object val = valueCache.get(Key.DisabledIds);

        if(dao == null){
            dao = new UserDao(context);
        }

        if(val == null){
            val = dao.getDisabledIds();
            valueCache.put(Key.DisabledIds, val);
        }

        //noinspection unchecked
        return (List<String>) val;
    }

    //====================================
    //保存是否消息免打扰
    public void setIsDisturb(boolean paramBoolean) {
        PreferenceManager.getInstance().setIsDisturb(paramBoolean);
    }
    //获取是否消息免打扰
    public boolean getIsDisturb() {
        return PreferenceManager.getInstance().getIsDisturb();
    }
    //==================================

    //----------------------------------------

    /*****
     * 消息免打扰的时间设定
     * @param startTime
     */
    public void setSettingDistrubStartTime(String startTime){
        PreferenceManager.getInstance().setSettingDistrubStartTime(startTime);
    }
    public void setSettingDistrubEndTime(String endTime){
        PreferenceManager.getInstance().setSettingDistrubEndTime(endTime);
    }

    public String getSettingDistrubStartTime(){
      return   PreferenceManager.getInstance().getSettingDistrubStartTime();
    }

    public String getSettingDistrubEndTime(){
       return PreferenceManager.getInstance().getSettingDistrubEndTime();
    }

    //----------------------------------------

    /***
     * 设置组同步
     * @param synced
     */
    public void setGroupsSynced(boolean synced){
        PreferenceManager.getInstance().setGroupsSynced(synced);
    }

    /****
     * 获取是否设置组同步
     * @return
     */
    public boolean isGroupsSynced(){
        return PreferenceManager.getInstance().isGroupsSynced();
    }

    /***
     * 设置联系人同步
     * @param synced
     */
    public void setContactSynced(boolean synced){
        PreferenceManager.getInstance().setContactSynced(synced);
    }

    /***
     * 获取是否设置联系人同步
     * @return
     */
    public boolean isContactSynced(){
        return PreferenceManager.getInstance().isContactSynced();
    }

    /***
     * 设置黑名单同步
     * @param synced
     */
    public void setBlacklistSynced(boolean synced){
        PreferenceManager.getInstance().setBlacklistSynced(synced);
    }

    /***
     * 获取是否设置黑名单同步
     * @return
     */
    public boolean isBacklistSynced(){
        return PreferenceManager.getInstance().isBacklistSynced();
    }

    /***
     * 设置是否允许聊天室主人离开
     * @param value
     */
    public void allowChatroomOwnerLeave(boolean value){
        PreferenceManager.getInstance().setSettingAllowChatroomOwnerLeave(value);
    }

    /***
     * 获取是否设置了允许聊天室主人离开
     * @return
     */
    public boolean isChatroomOwnerLeaveAllowed(){
        return PreferenceManager.getInstance().getSettingAllowChatroomOwnerLeave();
    }

    /****
     * 设置当退出群组时删除聊天记录
     * @param value
     */
    public void setDeleteMessagesAsExitGroup(boolean value) {
        PreferenceManager.getInstance().setDeleteMessagesAsExitGroup(value);
    }

    /****
     * 获取是否设置了当退出群组时删除聊天记录
     * @return
     */
    public boolean isDeleteMessagesAsExitGroup() {
        return PreferenceManager.getInstance().isDeleteMessagesAsExitGroup();
    }

    /****
     * 设置是否自动同意群组加群的邀请
     * @param value
     */
    public void setAutoAcceptGroupInvitation(boolean value) {
        PreferenceManager.getInstance().setAutoAcceptGroupInvitation(value);
    }

    /***
     * 获取是否设置了自动同意群组加群的邀请
     * @return
     */
    public boolean isAutoAcceptGroupInvitation() {
        return PreferenceManager.getInstance().isAutoAcceptGroupInvitation();
    }

    /***
     * 设置视频自适应码率编码
     * @param value
     */
    public void setAdaptiveVideoEncode(boolean value) {
        PreferenceManager.getInstance().setAdaptiveVideoEncode(value);
    }

    /****
     * 获取是否设置了视频自适应码率编码
     * @return
     */
    public boolean isAdaptiveVideoEncode() {
        return PreferenceManager.getInstance().isAdaptiveVideoEncode();
    }

    /****
     * 设置是否离线推送
     * @param value
     */
    public void setPushCall(boolean value) {
        PreferenceManager.getInstance().setPushCall(value);
    }

    /***
     * 获取是否设置了离线推送
     * @return
     */
    public boolean isPushCall() {
        return PreferenceManager.getInstance().isPushCall();
    }

    /***
     * 设置rest服务
     * @param restServer
     */
    public void setRestServer(String restServer){
        PreferenceManager.getInstance().setRestServer(restServer);
    }

    /***
     * 获取rest服务
     * @return
     */
    public String getRestServer(){
        return  PreferenceManager.getInstance().getRestServer();
    }

    /****
     * 设置IM服务
     * @param imServer
     */
    public void setIMServer(String imServer){
        PreferenceManager.getInstance().setIMServer(imServer);
    }

    /***
     * 获取IM服务
     * @return
     */
    public String getIMServer(){
        return PreferenceManager.getInstance().getIMServer();
    }

    /****
     * 设置是否自定义服务
     * @param enable
     */
    public void enableCustomServer(boolean enable){
        PreferenceManager.getInstance().enableCustomServer(enable);
    }

    /***
     * 获取是否自定义了服务
     * @return
     */
    public boolean isCustomServerEnable(){
        return PreferenceManager.getInstance().isCustomServerEnable();
    }

    /****
     * 设置是否可以自定义appKEY
     * @param enable
     */
    public void enableCustomAppkey(boolean enable) {
        PreferenceManager.getInstance().enableCustomAppkey(enable);
    }

    /****
     * 获取是否设置了可以自定义appkey
     * @return
     */
    public boolean isCustomAppkeyEnabled() {
        return PreferenceManager.getInstance().isCustomAppkeyEnabled();
    }

    /***
     * 设置自定义appkey
     * @param appkey
     */
    public void setCustomAppkey(String appkey) {
        PreferenceManager.getInstance().setCustomAppkey(appkey);
    }

    /****
     * 获取自定义的appkey
     * @return
     */
    public String getCutomAppkey() {
        return PreferenceManager.getInstance().getCustomAppkey();
    }

    /****
     * 获取是否设置了 漫游消息
     * @return
     */
    public boolean isMsgRoaming() {
        return PreferenceManager.getInstance().isMsgRoaming();
    }

    /****
     * 设置是否可以消息漫游
     * @param roaming
     */
    public void setMsgRoaming(boolean roaming) {
        PreferenceManager.getInstance().setMsgRoaming(roaming);
    }

    /****
     * 保存聊天背景图片
     * @param picPath_
     */
    public void setChatBackGroundPicUrl(String picPath_) {

        PreferenceManager.getInstance().setChatBackGroundPicUrl(picPath_);

    }

    /****
     * 获取设置好的聊天背景
     * @return
     */
    public String getChatBackGroundPicUrl() {
        return PreferenceManager.getInstance().GetChatBackGroundPicUrl();
    }




    enum Key{
        VibrateAndPlayToneOn,
        VibrateOn,
        PlayToneOn,
        SpakerOn,
        DisabledGroups,
        DisabledIds
    }


    /****
     * 判断当前时间是否在设定的时间内
     * @param startStr
     * @param endStr
     * @param current
     */
    public  boolean adjustTime(String startStr,String endStr,String current) {
        String[] startArr= startStr.split(":");
        String hourstart=startArr[0];
        String secondstart=startArr[1];
        //---------------------------------
        String[] endArr= endStr.split(":");
        String hourend=endArr[0];
        String secondend=endArr[1];
        //-----------------------------------
        String[] currentArr= current.split(":");
        String hourcurrent=currentArr[0];
        String secondcurrent=currentArr[1];

        if((Integer.parseInt(hourstart)<Integer.parseInt(hourend))||
                ((Integer.parseInt(hourstart)==Integer.parseInt(hourend))&&(Integer.parseInt(secondstart)<Integer.parseInt(secondend)))){
            //结束时间大于开始时间  当天
            int minuteOfDay=Integer.parseInt(hourcurrent)*60+Integer.parseInt(secondcurrent);
            int start_=Integer.parseInt(hourstart)*60+Integer.parseInt(secondstart);
            int end_=Integer.parseInt(hourend)*60+Integer.parseInt(secondend);
            if (minuteOfDay >= start_ && minuteOfDay <= end_) {
               // Toast.makeText(context.this, "在外围内", Toast.LENGTH_SHORT).show();
                Log.i("modd","在外围内");
                //消息免打扰
                return true;

            } else {
                Log.i("modd","在外围外");
                //Toast.makeText(MainActivity.this, "在外围外", Toast.LENGTH_SHORT).show();
                return false;

            }
        }else {
           // Toast.makeText(MainActivity.this, "跨天了", Toast.LENGTH_SHORT).show();
            //当前的时间  在 设定的开始时间 到24点之间     或0点到设定的结束时间之间
            int minuteOfDay=Integer.parseInt(hourcurrent)*60+Integer.parseInt(secondcurrent);
            //---------------
            int start_before=Integer.parseInt(hourstart)*60+Integer.parseInt(secondstart);
            int end_before=24*60;

            int start_end=0*60;
            int end_end=Integer.parseInt(hourend)*60+Integer.parseInt(secondend);
            //--------------------------
            if ((minuteOfDay >= start_before && minuteOfDay <= end_before)||(minuteOfDay>=start_end&&minuteOfDay<end_end)) {
               // Toast.makeText(MainActivity.this, "在外围内==跨天", Toast.LENGTH_SHORT).show();
                //消息免打扰
                Log.i("modd","在外围内==跨天");
                return true;
            } else {
               // Toast.makeText(MainActivity.this, "在外围外==跨天", Toast.LENGTH_SHORT).show();
                Log.i("modd","在外围外==跨天");
                return false;
            }
        }
    }
}

<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8"/>
    <title>邮件通知</title>
</head>
<body>

<div>
    <includetail>
        <div align="center">
            <div class="open_email" style="margin-left: 8px; margin-top: 8px; margin-bottom: 8px; margin-right: 8px;">
                <div>
                    <br>
                    <span class="genEmailContent">
                        <div id="cTMail-Wrap"
                             style="word-break: break-all;box-sizing:border-box;text-align:center;min-width:320px; max-width:660px; border:1px solid #f6f6f6; background-color:#f7f8fa; margin:auto; padding:20px 0 30px; font-family:'helvetica neue',PingFangSC-Light,arial,'hiragino sans gb','microsoft yahei ui','microsoft yahei',simsun,sans-serif">
                            <div class="main-content" style="">
                                <table style="width:100%;font-weight:300;margin-bottom:10px;border-collapse:collapse">
                                    <tbody>
                                    <tr style="font-weight:300">
                                        <td style="width:3%;max-width:30px;"></td>
                                        <td style="max-width:600px;">
                                            <div id="cTMail-logo" style="width:92px; height:25px;">
                                                <a href="">
                                                    <img border="0" src="https://image.youyoushop.work/yahoo/zhenyu.png"
                                                         style="height:50px;display:block">
                                                </a>
                                            </div>
                                            <p style="height:2px;background-color: #00a4ff;border: 0;font-size:0;padding:0;width:100%;margin-top:20px;"></p>

                                            <div id="cTMail-inner"
                                                 style="background-color:#fff; padding:23px 0 20px;box-shadow: 0px 1px 1px 0px rgba(122, 55, 55, 0.2);text-align:left;">
                                                <table style="width:100%;font-weight:300;margin-bottom:10px;border-collapse:collapse;text-align:left;">
                                                    <tbody>
                                                    <tr style="font-weight:300">
                                                        <td style="width:3.2%;max-width:30px;"></td>
                                                        <td style="max-width:480px;text-align:left;">
                                                            <h1 id="cTMail-title"
                                                                style="font-size: 20px; line-height: 36px; margin: 0px 0px 22px;">
                                                                邮件测试平台
                                                            </h1>
                                                            <p id="cTMail-receiverName"
                                                               style="font-size:14px;color:#333; line-height:24px; margin:0;">
                                                                尊敬的<span style="font-weight: bold;">&nbsp;${receiverName}&nbsp;</span>用户，您好！
                                                            </p>

                                                            <p class="cTMail-content"
                                                               style="line-height: 24px; margin: 6px 0px 0px; overflow-wrap: break-word; word-break: break-all;">
                                                                <span style="color: rgb(51, 51, 51); font-size: 14px;">
                                                                    由<span style="font-weight: bold;">&nbsp;${fromName}&nbsp;</span>发送的邮件请及时处理。消息内容如下：<br/>
                                                                </span>
                                                            </p>

                                                            <p class="cTMail-content"
                                                               style="line-height: 24px; margin: 6px 0px 0px; overflow-wrap: break-word; word-break: break-all;">
                                                                <span style="color: rgb(51, 51, 51); font-size: 14px;">
                                                                    <span style="font-weight: bold;">${content}</span>
                                                                </span>
                                                            </p>

                                                            <dl style="font-size: 14px; color: rgb(51, 51, 51); line-height: 18px;">
                                                                <dd style="margin: 0px 0px 6px; padding: 0px; font-size: 12px; line-height: 22px;">
                                                                    <p id="cTMail-sender"
                                                                       style="font-size: 14px; line-height: 26px; word-wrap: break-word; word-break: break-all; margin-top: 32px;">
                                                                        发送时间
                                                                        <br>
                                                                        <strong>${sendTime}</strong>
                                                                    </p>
                                                                </dd>
                                                            </dl>
                                                        </td>
                                                        <td style="width:3.2%;max-width:30px;"></td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>

                                        </td>
                                        <td style="width:3%;max-width:30px;"></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </span>
                </div>
            </div>
        </div>
    </includetail>
</div>

</body>
</html>
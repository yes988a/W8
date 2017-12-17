package com.w8;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.w8.base.AppUtil;
import com.w8.base.MyApp;
import com.w8.base.OnlineActivity;
import com.w8.base.TimUtil;
import com.w8.base.WxUtil;
import com.w8.base.data.Active;
import com.w8.base.data.ActiveDao;
import com.w8.base.data.Chat;
import com.w8.base.data.ChatDao;
import com.w8.base.data.EditeMsg;
import com.w8.base.data.EditeMsgDao;
import com.w8.base.data.Friend;
import com.w8.base.data.FriendDao;
import com.w8.base.data.Group;
import com.w8.base.data.GroupDao;
import com.w8.base.data.Smile;
import com.w8.base.event.Ret_chat;
import com.w8.base.pcurl.ChatUtil;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.greenrobot.eventbus.ThreadMode.*;

/**
 * 单人聊天，
 * 单人聊天和群聊分开。主要二者表不一样，单聊有是不是我好友，单聊不显示好友名称，发送URL不一样，跳转到的设置页面不一样，标题不一样，以后可能扩展不也一样
 */
public class ChatallActivity extends OnlineActivity implements View.OnClickListener {

    public final static String para_uuid = "fd1ad";

    private final String INSTANCE_CAMERA_FILE_PATH = "INSTANCE_CAMERA_FILE_PATH";//调用相机使用。图片返回的路径，暂存标示
    private final int ACTIVITY_REQUEST_CAMERA = 2420;//调用相机ativity之间传递的参数
    private final int PERMISSION_REQUEST_CAMERA = 2621;//照相权限标示
    private final int ACTIVITY_REQUEST_SELECT_PHOTO = 3246;//调用相册选择器时，传递的参数，应该是可以随便写
    private final String TEMP_IMG_NAME = "yyyyMMdd_HHmmssSSS";//照相临时图片名称格式

    private String mCameraFilePath;//拍照路径
    private final int txt_left = 316;//文本，左
    private final int txt_right = 326;//文本，右
    private final int img_left = 416;//图片，左
    private final int img_right = 426;//图片，右
    private final int back_left = 617;//撤回，左
    private final int back_right = 627;//撤回，右
    private final int chat_err = 555;//错误，什么也不显示

    private RecyclerView mRecycler_smile; //表情
    private AdapterSmile adapter_simile;  //表情
    private List<Smile> smileList;        //表情初始化使用
    private final Map<String, Integer> smileMap = new HashMap<String, Integer>();//表情初始化使用

    private String uuid;//群ID、好友ID（用于查看详情和设置，和存储信息）
    private String myid;//我的ID
    private Boolean boo;// true好友，false群。

    private EditText chat_message;//聊天编辑框

    private RecyclerView mRecyclerView_chat; //聊天
    private LinearLayoutManager lmac;        //聊天
    private AdapterChat adapter;             //聊天

    private ImageView chat_photograph;
    private ImageView chat_album;
    private ImageView chat_sound;
    private ImageView chat_position;
    private LinearLayout chatsingle_more_linear;

    private List<Chat> datas;                //聊天数据。

    private Button chat_smile_keyboard_but; //表情，按钮。
    private Button chat_more;               //更多。按钮
    private Button chat_send;               //发送按钮

    private int isFri;//是不是我的好友。true表示好友，false表示群。
    private String remark;  //群名称，或者，好友名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatall);
        TAG = ChatallActivity.class.getSimpleName();
        if (savedInstanceState != null) {
            mCameraFilePath = savedInstanceState.getString(INSTANCE_CAMERA_FILE_PATH);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        setSupportActionBar(toolbar);

        myid = AppUtil.getUid();
        //初始化公用view。暂时只有：聊天编辑框、表情构造器
        mRecycler_smile = (RecyclerView) findViewById(R.id.mRecycler_smile);
        mRecycler_smile.setLayoutManager(new GridLayoutManager(this, 6));//这里的7 应该根据屏幕宽度除以每个表情的宽得到可变动数字
        adapter_simile = new AdapterSmile();//笑脸适配器
        mRecycler_smile.setAdapter(adapter_simile);
        mRecycler_smile.setHasFixedSize(true);

        chat_smile_keyboard_but = (Button) findViewById(R.id.chat_smile_keyboard_but);
        chat_more = (Button) findViewById(R.id.chat_more);
        chat_send = (Button) findViewById(R.id.chat_send);

        mRecyclerView_chat = (RecyclerView) findViewById(R.id.mRecyclerView_chat);

        lmac = new LinearLayoutManager(this);
        mRecyclerView_chat.setLayoutManager(lmac);
        adapter = new AdapterChat();
        mRecyclerView_chat.setAdapter(adapter);
//        mRecyclerView_chat.setHasFixedSize(true);

        chat_photograph = (ImageView) findViewById(R.id.chat_photograph);
        chat_album = (ImageView) findViewById(R.id.chat_album);
        chat_sound = (ImageView) findViewById(R.id.chat_sound);
        chat_position = (ImageView) findViewById(R.id.chat_position);
        chatsingle_more_linear = (LinearLayout) findViewById(R.id.chatsingle_more_linear);

        chat_message = (EditText) findViewById(R.id.chat_message);

        initListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle == null) {
            this.finish();
            return;
        } else {
            uuid = bundle.getString(para_uuid);
            isFri = bundle.getInt(AppUtil.para_boolean);
            if (uuid == null || "".equals(uuid) || "".equals(myid)) {
                //错误分析。
                finish();
                return;
            } else {
                if (ChatUtil.url_app_findChatsingle == isFri) { // 好友。
                    List<Friend> cc = MyApp.mC.getDS().getFriendDao().queryBuilder()
                            .where(FriendDao.Properties.Fid.eq(uuid)).list();
                    if (cc.size() == 1) {
                        remark = cc.get(0).getRemark();
                        initFirst();
                    } else {
                        //分析错误
                        finish();
                        return;
                    }
                } else { // 群。
                    List<Group> cc = MyApp.mC.getDS().getGroupDao().queryBuilder()
                            .where(GroupDao.Properties.Gid.eq(uuid)).list();
                    if (cc.size() == 1) {
                        remark = cc.get(0).getGremark();
                        initFirst();
                    } else {
                        //分析错误
                        finish();
                        return;
                    }
                }
            }
        }
    }

    private void initFirst() {
        toolbar.setTitle(remark);
        initList();
        initEditeMsg();
    }

    //刷新
    @Subscribe(threadMode = MAIN)
    public void c(Ret_chat chat) {
        //判断是否当前好友。
        initList();
    }

    /**
     * 有新数据时刷新列表，不改变交表位置
     * <p>
     * 情况下使用，一、接收到新信息，二、我发送新内容时，三、消息发送完成，如果消息失败，四、列表上拉显示更多信息
     */
    private void initList() {
        datas = MyApp.mC.getDS().getChatDao().queryBuilder()
                .whereOr(ChatDao.Properties.Requid.eq(uuid), ChatDao.Properties.Resuid.eq(uuid))
                .orderAsc(ChatDao.Properties.Tim).list();
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quit();
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_chat_details: //详情。
                        if (ChatUtil.url_app_findChatsingle == isFri) { // 好友
                            AppUtil.toFriDes(uuid);
                        } else {    //群
                            AppUtil.toGroDes(uuid);
                        }
                        break;
                    case R.id.action_chat_remove: //active活动列表中移除。
                        // active中移除，并关闭当前页
                        ActiveDao adRemo = MyApp.mC.getDS().getActiveDao();
                        adRemo.deleteInTx(adRemo.queryBuilder().where(ActiveDao.Properties.Uuid.eq(uuid)).list());
                        finish();
                        break;
                    case R.id.action_chat_clear:
                        AppUtil.clearOne(uuid);
                        finish();
                        break;
                }
                return false;
            }
        });

        chat_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    chat_send.setVisibility(View.GONE);
                    chat_more.setVisibility(View.VISIBLE);
                } else {
                    chat_send.setVisibility(View.VISIBLE);
                    chat_more.setVisibility(View.GONE);
                }
            }
        });
        chat_message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_SEND) {
//                    sendMsg();发送消息
                    return true;
                } else {
                    return false;
                }
            }
        });

        chat_message.setOnClickListener(this);
        chat_photograph.setOnClickListener(this);
        chat_album.setOnClickListener(this);
        chat_sound.setOnClickListener(this);
        chat_position.setOnClickListener(this);
        chat_smile_keyboard_but.setOnClickListener(this);
        chat_more.setOnClickListener(this);
        chat_send.setOnClickListener(this);
    }

    /**
     * 界面固定按钮点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.chat_txt_head_right || id == R.id.chat_img_head_right) {//我的头像，跳转到我的个人详情
            Log.e(TAG, "点击我的头像");
        } else if (id == R.id.chat_txt_head_left || id == R.id.chat_img_head_left) {//跳转到好友详情
            Log.e(TAG, "好友的头像");
        } else if (id == R.id.chat_photograph) {//拍照
            takePhoto();
        } else if (id == R.id.chat_album) {//相册选择
            selectImage();
        } else if (id == R.id.chat_sound) {//录音

        } else if (id == R.id.chat_smile_keyboard_but) {//笑脸 和 键盘切换按钮
            if (mRecycler_smile.getVisibility() == View.VISIBLE) {
                mRecycler_smile.setVisibility(View.GONE);
                chat_smile_keyboard_but.setBackgroundResource(R.drawable.ic_smiling);
            } else {
                mRecycler_smile.setVisibility(View.VISIBLE);
                chat_smile_keyboard_but.setBackgroundResource(R.drawable.ic_keyboard);
                if (smileList == null || smileList.size() == 0) {
//                    initSimlie();
                }
            }
            if (!chat_message.hasFocus()) {//如果没有焦点，获取焦点，并隐藏键盘
                chat_message.setFocusable(true);
                chat_message.setFocusableInTouchMode(true);
                chat_message.requestFocus();
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
            if (isOpen) {
                imm.hideSoftInputFromInputMethod(chat_message.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
            if (chatsingle_more_linear.getVisibility() == View.VISIBLE) {
                chatsingle_more_linear.setVisibility(View.GONE);
            }
            scrollerDown();
        } else if (id == R.id.chat_more) {//更多
            if (View.VISIBLE == chatsingle_more_linear.getVisibility()) {
                hideAll();
            } else {
                mRecycler_smile.setVisibility(View.GONE);
                chatsingle_more_linear.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromInputMethod(chat_message.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
            scrollerDown();
        } else if (id == R.id.chat_send) {//发送信息
            String chatmess = chat_message.getText().toString();
            sendMess(chatmess);
        } else if (id == R.id.chat_message) {
            chatsingle_more_linear.setVisibility(View.GONE);
            scrollerDown();
        }
    }

    /**
     * 滚动到底部
     */
    private void scrollerDown() {
        int count = adapter.getItemCount();
        if (count > 3) {
            mRecyclerView_chat.smoothScrollToPosition(count - 1);
        }
    }

    /**
     * 向上滚动，隐藏所有按钮：如，表情，键盘，更多
     */
    private void hideAll() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        if (!isOpen) {
            imm.showSoftInputFromInputMethod(chat_message.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
        }
        mRecycler_smile.setVisibility(View.GONE);
        chat_smile_keyboard_but.setBackgroundResource(R.drawable.ic_smiling);
        if (chatsingle_more_linear.getVisibility() == View.VISIBLE) {
            chatsingle_more_linear.setVisibility(View.GONE);
        }
    }

/*--------------------         适配器   所有ViewHolder   -------------------*/

    /**
     * 聊天适配器
     */
    class AdapterChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (txt_left == viewType) {
                return new VH_txt_left(LayoutInflater.from(ChatallActivity.this).inflate(R.layout.recycler_chat_txt_left, parent, false));
            } else if (txt_right == viewType) {
                return new VH_txt_right(LayoutInflater.from(ChatallActivity.this).inflate(R.layout.recycler_chat_txt_right, parent, false));
            } else if (img_left == viewType) {
                return new VH_img_left(LayoutInflater.from(ChatallActivity.this).inflate(R.layout.recycler_chat_img_left, parent, false));
            } else if (img_right == viewType) {
                return new VH_img_right(LayoutInflater.from(ChatallActivity.this).inflate(R.layout.recycler_chat_img_right, parent, false));
            } else if (back_left == viewType || back_right == viewType) {
                return new VH_back(LayoutInflater.from(ChatallActivity.this).inflate(R.layout.recycler_chat_back, parent, false));
            } else {
                return new VH_err(LayoutInflater.from(ChatallActivity.this).inflate(R.layout.recycler_chat_err, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            if (position + 1 == datas.size() && !(defaultNum > datas.size())) {
//                defaultNum += defaultNum;
//                datas = app.daoSession.getChatDao().queryBuilder().whereOr(ChatDao.Properties.Requid.eq(uuid), ChatDao.Properties.Resuid.eq((uuid))).orderAsc(ChatDao.Properties.Tim).limit(defaultNum).list();
//            }
            int viewType = holder.getItemViewType();
            final Chat cs = datas.get(position);
            if (txt_left == viewType) {
                VH_txt_left vh = (VH_txt_left) holder;
                vh.chat_txt_left_id.setText(cs.getTim() + "");
                vh.chat_txt_head_left.setOnClickListener(ChatallActivity.this);
                Picasso.with(ChatallActivity.this).load("http://img3.duitang.com/uploads/item/201509/04/20150904123004_5RfsX.thumb.700_0.jpeg")
                        .error(R.drawable.img_err).into(vh.chat_txt_head_left);
                final String txt = cs.getTxt();
                vh.txt_left.setText(AppUtil.getExpressionString(txt, smileMap));
                vh.txt_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       /* Intent intent = new Intent(ChatallActivity.this, ChatBigTxtActivity.class);
//                        intent.putExtra(WxUtil.txt, txt);
                        startActivity(intent);*/
                    }
                });
            } else if (txt_right == viewType) {
                VH_txt_right vh = (VH_txt_right) holder;
                vh.chat_txt_right_id.setText(cs.getTim() + "");
                vh.chat_txt_head_right.setOnClickListener(ChatallActivity.this);
                Picasso.with(ChatallActivity.this).load("http://img3.duitang.com/uploads/item/201509/04/20150904123004_5RfsX.thumb.700_0.jpeg")
                        .error(R.drawable.img_err).into(vh.chat_txt_head_right);
                final String txt = cs.getTxt();
                vh.txt_right.setText(AppUtil.getExpressionString(txt, smileMap));
                /*if (WxUtil.c_err == cs.getStat()) {//发送失败
                    vh.chat_txt_send_err_right.setVisibility(View.VISIBLE);
                    vh.progress_chat_txt_right.setVisibility(View.GONE);
                    vh.chat_txt_send_err_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendMess(txt);
                        }
                    });
                } else if (WxUtil.c_a == cs.getStat()) {//发送中
                    vh.progress_chat_txt_right.setVisibility(View.VISIBLE);
                    vh.chat_txt_send_err_right.setVisibility(View.GONE);
                } else {
                    vh.progress_chat_txt_right.setVisibility(View.GONE);
                    vh.chat_txt_send_err_right.setVisibility(View.GONE);
                }
                vh.txt_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ChatallActivity.this, ChatBigTxtActivity.class);
                        intent.putExtra(WxUtil.txt, txt);
                        intent.putExtra(WxUtil.csid, cs.getUuid());
                        intent.putExtra(WxUtil.tim, cs.getTim());
                        intent.putExtra(WxUtil.fid, (uuid));
                        intent.putExtra(WxUtil.url, url);
                        startActivity(intent);
                    }
                });*/
            } else if (img_left == viewType) {
                VH_img_left vh = (VH_img_left) holder;
                vh.chat_img_left_id.setText(cs.getTim() + "");//图片地址为主键ID
                vh.chat_img_head_left.setOnClickListener(ChatallActivity.this);
                File FRIHea = AppUtil.getFile(uuid + ".JPG");
                Picasso.with(ChatallActivity.this).load(FRIHea).error(R.drawable.img_err).into(vh.chat_img_head_left);
                final String imgPath = cs.getTxt();
                File file = AppUtil.getFile(imgPath);
                Picasso.with(ChatallActivity.this).load(file).error(R.drawable.img_err).into(vh.img_left);
                vh.img_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       /* Intent intent = new Intent(ChatallActivity.this, ChatBigimgActivity.class);
//                        intent.putExtra(WxUtil.img_path, imgPath);
                        startActivity(intent);*/
                    }
                });
            } else if (img_right == viewType) {
                VH_img_right vh = (VH_img_right) holder;
                vh.chat_img_head_right.setOnClickListener(ChatallActivity.this);
                File MYHea = AppUtil.getFile(myid + ".JPG");
                Picasso.with(ChatallActivity.this).load(MYHea).error(R.drawable.head_no).into(vh.chat_img_head_right);
                final String imgPath = cs.getTxt();
                File fileImge = AppUtil.getFile(imgPath);
                Picasso.with(ChatallActivity.this).load(fileImge).error(R.drawable.img_err).into(vh.img_right);
                /*vh.img_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ChatallActivity.this, ChatBigimgActivity.class);
                        intent.putExtra(WxUtil.img_path, imgPath);
                        intent.putExtra(WxUtil.csid, cs.getUuid());
                        intent.putExtra(WxUtil.tim, cs.getTim());
                        intent.putExtra(WxUtil.fid, (uuid));
                        intent.putExtra(WxUtil.url, url);
                        startActivity(intent);
                    }
                });
                if (WxUtil.c_err == cs.getStat()) {//发送失败
                    vh.chat_img_send_err_right.setVisibility(View.VISIBLE);
                    vh.progress_chat_img_right.setVisibility(View.GONE);
                    vh.chat_img_send_err_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendOneImg(imgPath);
                        }
                    });
                } else if (WxUtil.c_a == cs.getStat()) {//发送中
                    vh.progress_chat_img_right.setVisibility(View.VISIBLE);
                    vh.chat_img_send_err_right.setVisibility(View.GONE);
                } else {
                    vh.progress_chat_img_right.setVisibility(View.GONE);
                    vh.chat_img_send_err_right.setVisibility(View.GONE);
                }*/
            } else if (back_left == viewType) {//好友撤回消息
                VH_back vh = (VH_back) holder;
                vh.chat_back_text.setText(getString(R.string.back_left));
            } else if (back_right == viewType) {//我撤回消息
                VH_back vh = (VH_back) holder;
                vh.chat_back_text.setText(getString(R.string.back_right));
            } else {
                VH_err vh = (VH_err) holder;
            }

            int lastPosition = lmac.findLastVisibleItemPosition();
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public int getItemViewType(int position) {

            Chat cs = datas.get(position);
/*
            if (WxUtil.c_back == cs.getStat()) {
                if (myid.equals(cs.getRequid())) {
                    return back_right;
                } else {
                    return back_left;
                }
            } else {
                if (myid.equals(cs.getRequid())) {
                    int typ = cs.getTyp();
                    if (WxUtil.c_txt == typ) {//我
                        return txt_right;
                    } else if (WxUtil.c_img == typ) {
                        return img_right;
                    } else {
                        return 0;//待处理
                    }
                } else {//好友
                    int typ = cs.getTyp();
                    if (WxUtil.c_txt == typ) {
                        return txt_left;
                    } else if (WxUtil.c_img == typ) {
                        return img_left;
                    } else {
                        return chat_err;//待处理
                    }
                }
            }*/
            return 0;
        }

        /**
         * 好友文本
         */
        class VH_txt_left extends RecyclerView.ViewHolder {
            TextView txt_left;
            ImageView chat_txt_head_left;
            TextView chat_txt_left_id;

            public VH_txt_left(View itemView) {
                super(itemView);
                chat_txt_head_left = (ImageView) itemView.findViewById(R.id.chat_txt_head_left);
                txt_left = (TextView) itemView.findViewById(R.id.chat_txt_left);
                chat_txt_left_id = (TextView) itemView.findViewById(R.id.chat_txt_left_id);
            }
        }

        /**
         * 我的文本
         */
        class VH_txt_right extends RecyclerView.ViewHolder {
            ImageView chat_txt_head_right;
            TextView txt_right;
            TextView chat_txt_right_id;
            ImageView chat_txt_send_err_right;
            ProgressBar progress_chat_txt_right;

            public VH_txt_right(View itemView) {
                super(itemView);
                chat_txt_head_right = (ImageView) itemView.findViewById(R.id.chat_txt_head_right);
                txt_right = (TextView) itemView.findViewById(R.id.chat_txt_right);
                chat_txt_right_id = (TextView) itemView.findViewById(R.id.chat_txt_right_id);
                chat_txt_send_err_right = (ImageView) itemView.findViewById(R.id.chat_txt_send_err_right);
                progress_chat_txt_right = (ProgressBar) itemView.findViewById(R.id.progress_chat_txt_right);
            }
        }

        /**
         * 好友图片
         */
        class VH_img_left extends RecyclerView.ViewHolder {
            ImageView chat_img_head_left;
            TextView chat_img_left_id;
            ImageView img_left;

            public VH_img_left(View itemView) {
                super(itemView);
                chat_img_head_left = (ImageView) itemView.findViewById(R.id.chat_img_head_left);
                chat_img_left_id = (TextView) itemView.findViewById(R.id.chat_img_left_id);
                img_left = (ImageView) itemView.findViewById(R.id.chat_img_left);
            }
        }

        /**
         * 我的图片
         */
        class VH_img_right extends RecyclerView.ViewHolder {
            ImageView chat_img_head_right;
            ImageView img_right;
            ImageView chat_img_send_err_right;
            TextView chat_img_right_id;
            ProgressBar progress_chat_img_right;


            public VH_img_right(View itemView) {
                super(itemView);
                chat_img_send_err_right = (ImageView) itemView.findViewById(R.id.chat_img_send_err_right);
                chat_img_head_right = (ImageView) itemView.findViewById(R.id.chat_img_head_right);
                chat_img_right_id = (TextView) itemView.findViewById(R.id.chat_img_right_id);
                img_right = (ImageView) itemView.findViewById(R.id.chat_img_right);
                progress_chat_img_right = (ProgressBar) itemView.findViewById(R.id.progress_chat_img_right);
            }
        }

        /**
         * 我的图片
         */
        class VH_back extends RecyclerView.ViewHolder {
            TextView chat_back_text;

            public VH_back(View itemView) {
                super(itemView);
                chat_back_text = (TextView) itemView.findViewById(R.id.chat_back_text);
            }
        }

        /**
         * 错误
         */
        class VH_err extends RecyclerView.ViewHolder {

            public VH_err(View itemView) {
                super(itemView);
            }
        }
    }

    /**
     * 选择图片。
     */
    private void selectImage() {
        // 1. 使用默认风格，并指定选择数量：
        // 第一个参数Activity/Fragment； 第二个request_code； 第三个允许选择照片的数量，不填可以无限选择。
        // Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO, 9);

        // 2. 使用默认风格，不指定选择数量：
        // Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO); // 第三个参数不填的话，可以选择无数个。

        // 3. 指定风格，并指定选择数量，如果不想限制数量传入Integer.MAX_VALUE;
       /* Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO
                , 6                                                         // 指定选择数量。
                , ContextCompat.getColor(this, R.color.colorAccent)        // 指定Toolbar的颜色。
                , ContextCompat.getColor(this, R.color.colorPrimaryDark));  // 指定状态栏的颜色。*/
    }

    /**
     * 处理选择的照片。
     */
    private void handleSelectImage(List<String> pathList) {

/*

        File compressedImage = new Compressor(this)
                .setMaxWidth(640)
                .setMaxHeight(480)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .compressToFile(actualImage);




        Compressor comp = Compressor.getDefault(this);
        for (int i = 0; i < pathList.size(); i++) {
            File file = new File(pathList.get(i));
            if (file.length() > 100) {
                String txtU32 = WxUtil.getU32() + file.getName();
                File file1 = comp.compressToFile(file);
                //存入私有文件
                int retunum = AppUtil.setFile(this, file1, txtU32);
                if (-1 != retunum) {
                    sendOneImg(txtU32);
                }
            }
        }*/
        initList();
        scrollerDown();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVITY_REQUEST_CAMERA: {//拍照按钮回调
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent();
                    ArrayList<String> pathList = new ArrayList<>();
                    pathList.add(mCameraFilePath);
                    handleSelectImage(pathList);
                }
                break;
            }
            case ACTIVITY_REQUEST_SELECT_PHOTO: {//照片选择器回调
                if (resultCode == RESULT_OK) { // 成功选择了照片。
                    // 选择好了照片后，调用这个方法解析照片路径的List。
//                    handleSelectImage(Album.parseResult(data));
                    break;
                } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。
                    break;
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * 发送文本消息
     */
    private void sendMess(String txt) {
        chat_message.setText("");
        Chat chat = new Chat();
        /*chat.setStat(WxUtil.c_a);
        chat.setTxt(txt);
        chat.setTyp(WxUtil.c_txt);
        chat.setRequid(myid);
        chat.setResuid(uuid);
        chat.setTim(System.currentTimeMillis() + numchat);
        String uid = getUID32();
        chat.setUuid(uid);
        final long lid = app.daoSession.getChatDao().insert(chat);
        ChatSingleInput csiput = new ChatSingleInput();
        csiput.setAuid(uid);
        csiput.setResuid(uuid);
        csiput.setTxt(txt);
        csiput.setTyp(WxUtil.c_txt);

        //此方法发送的都是文本和表情。不会有其他内容
        WXChatRequest stringRequest = new WXChatRequest(ChatallActivity.this, url + WxUtil.u_insertsingle, csiput, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                com.alibaba.fastjson.JSONObject jo = JSON.parseObject(response);
                Integer r = jo.getInteger(WxUtil.r);
                if (r != null && RetNumUtil.n_0 == r) {
                    Long timm = jo.getLong(WxUtil.tim);
                    String uid = jo.getString(WxUtil.uuid);
                    Chat chat = app.daoSession.getChatDao().load(lid);
                    chat.setStat(WxUtil.c_ab);
                    chat.setTim(timm);
                    chat.setUuid(uid);
                    app.daoSession.getChatDao().update(chat);
                    //刷新发送记录
                    initList();
                } else if (r != null && RetNumUtil.n_1 == r) {
                    TokenUtil.updateTo(app);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Chat chat = app.daoSession.getChatDao().load(lid);
                chat.setStat(WxUtil.c_err);//标记发送错误
                app.daoSession.getChatDao().update(chat);
                initList();
            }
        });
        app.getVolleyQeue().add(stringRequest);*/
        initList();
        scrollerDown();
    }

    private void sendOneImg(String privateName) {
        //插入数据库
        Chat chat = new Chat();
       /* chat.setStat(WxUtil.c_a);
        chat.setTxt(privateName);
        chat.setTyp(WxUtil.c_img);
        chat.setRequid(myid);
        chat.setResuid(uuid);
        chat.setTim(System.currentTimeMillis() + numchat);
        String uid = getUID32();
        chat.setUuid(uid);
        final long lid = app.daoSession.getChatDao().insert(chat);

        ChatSingleInput csiput = new ChatSingleInput();
        csiput.setAuid(uid);
        csiput.setResuid(uuid);
        csiput.setTxt(privateName);
        csiput.setTyp(WxUtil.c_img);
        File file1 = WxUtil.getFile(this, privateName);
        WXFileStringRequest wxfsr = new WXFileStringRequest(this, url + WxUtil.u_insertsingle, csiput, WxUtil.fileToBase64(file1), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                com.alibaba.fastjson.JSONObject jo = JSON.parseObject(response);
                Integer r = jo.getInteger(WxUtil.r);
                if (r != null && RetNumUtil.n_0 == r) {
                    Long timm = jo.getLong(WxUtil.tim);
                    String uid = jo.getString(WxUtil.uuid);
                    Chat chat = app.daoSession.getChatDao().load(lid);
                    chat.setStat(WxUtil.c_ab);
                    chat.setTim(timm);
                    chat.setUuid(uid);
                    app.daoSession.getChatDao().update(chat);
                    //刷新发送记录
                    initList();
                } else if (r != null && RetNumUtil.n_1 == r) {
                    TokenUtil.updateTo(app);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Chat chat = app.daoSession.getChatDao().load(lid);
                chat.setStat(WxUtil.c_err);//标记发送错误
                app.daoSession.getChatDao().update(chat);
                initList();
            }
        });
        app.getVolleyQeue().add(wxfsr);*/
    }


    /**
     * 无权限判断
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA: {
                int permissionResult = grantResults[0];
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.permission_failed)
                            .setMessage(R.string.permission_camera_failed_hint)
                            .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * 启动相机拍照。
     */
    private void startCamera() {
        /*String outFileFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        String outFilePath = AlbumUtils.getNowDateTime(TEMP_IMG_NAME) + ".jpg";
        File file = new File(outFileFolder, outFilePath);
        mCameraFilePath = file.getAbsolutePath();
//        AlbumUtils.startCamera(this, ACTIVITY_REQUEST_CAMERA, file);*/
    }

    /**
     * 拍照点击监听。
     */
    private void takePhoto() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else if (permissionResult == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            }
        } else {
            startCamera();
        }
    }

    /**
     * 这个方法好像是说当activity暂时退出啦，参数暂时保存，当回来时还能接着使用（onCreatez中savedInstanceState获取）
     *
     * @param outState
     * @param outPersistentState
     */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString(INSTANCE_CAMERA_FILE_PATH, mCameraFilePath);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * 表情适配器
     */
    class AdapterSmile extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AdapterSmile.VH_smile(LayoutInflater.from(ChatallActivity.this).inflate(R.layout.recycler_chat_smile, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
            AdapterSmile.VH_smile vh = (AdapterSmile.VH_smile) holder;
            final Smile sss = smileList.get(pos);
            vh.chat_smile_sname.setText(sss.getSname());
            try {
                vh.chat_smile.setImageResource(Integer.parseInt(R.drawable.class.getDeclaredField("f" + sss.getFnum()).get(null).toString()));
            } catch (Exception e) {
                mwErr(TAG, "vh.chat_smile.setImageResource", e);
            }

            vh.chat_smile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer dd = null;
                    try {
                        dd = Integer.parseInt(R.drawable.class.getDeclaredField("f" + sss.getFnum()).get(null).toString());
                    } catch (IllegalAccessException e) {
                        Log.e(TAG, e.getMessage());
                    } catch (NoSuchFieldException e) {
                        Log.e(TAG, e.getMessage());
                    }

                    if (dd != null) {
                        int index = chat_message.getSelectionStart();
                        Editable editable = chat_message.getText();
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), dd);
                        ImageSpan imageSpan = new ImageSpan(ChatallActivity.this, bitmap);//获取图片
                        SpannableString spannableString = new SpannableString(sss.getSname());
                        spannableString.setSpan(imageSpan, 0, sss.getSname().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//创建string
                        editable.insert(index, spannableString);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return smileList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        /**
         * 头像适配器
         */
        class VH_smile extends RecyclerView.ViewHolder {
            ImageView chat_smile;
            TextView chat_smile_sname;

            public VH_smile(View itemView) {
                super(itemView);
                chat_smile = (ImageView) itemView.findViewById(R.id.chat_smile_img);
                chat_smile_sname = (TextView) itemView.findViewById(R.id.chat_smile_sname);
            }
        }
    }

    //编辑中的内容初始化。
    private void initEditeMsg() {
        List<EditeMsg> listefg = MyApp.mC.getDS().getEditeMsgDao().queryBuilder()
                .where(EditeMsgDao.Properties.Uuid.eq(uuid)).list();
        if (listefg.size() == 1) {
            EditeMsg egor = listefg.get(0);
            if (egor.getTxt() != null && egor.getTxt().length() > 0) {
                chat_send.setVisibility(View.VISIBLE);
                chat_more.setVisibility(View.GONE);
                chat_message.setText(AppUtil.getExpressionString(egor.getTxt(), smileMap));
            }
        } else if (listefg.size() > 1) {
            MyApp.mC.getDS().getEditeMsgDao().deleteInTx(listefg);
        }
    }

    //退出后，更新待编辑内容。
    private void quitEditeMsg() {
        EditeMsgDao dao = MyApp.mC.getDS().getEditeMsgDao();
        List<EditeMsg> listegf = dao.queryBuilder().where(EditeMsgDao.Properties.Uuid.eq(uuid)).list();
        String chattxt = chat_message.getText().toString();
        if (chattxt == null || "".equals(chattxt)) {
            dao.deleteInTx(listegf);
        } else { // active，编辑信息。
            //类型，只有文本
            if (listegf.size() == 1) {
                listegf.get(0).setTxt(chattxt);
                dao.updateInTx(listegf);
            } else if (listegf.size() == 0) {
                EditeMsg egor = new EditeMsg();
                egor.setUuid(uuid);
                egor.setTim(System.currentTimeMillis());
                egor.setTxt(chattxt);
                dao.insert(egor);
            } else {
                dao.deleteInTx(listegf);
            }
        }
    }

    /**
     * 更新active列表（内容变动完成后执行。）
     * <p>
     * 一、有未发送内容，显示未发送内容。
     * <p>
     * 二、没有未发送内容，显示最后一条聊天内容（如果，最后一条消息也没有，移除active）。
     */
    private void quitActive() {
        EditeMsgDao dao = MyApp.mC.getDS().getEditeMsgDao();
        long num = dao.queryBuilder().where(EditeMsgDao.Properties.Uuid.eq(uuid)).count();
        if (num == 0) {
            if (datas.size() > 0) {
                ActiveDao activeDao = MyApp.mC.getDS().getActiveDao();
                List<Active> list = activeDao.queryBuilder().where(ActiveDao.Properties.Uuid.eq(uuid)).list();
                if (list.size() > 1) {
                    activeDao.deleteInTx(list);
                }
                Chat cc = datas.get(0);
                Active active = new Active();
                if (list.size() == 1) {
                    active = list.get(0);
                }
                active.setUuid(uuid);
                active.setTitle(remark);
                active.setNum(0);
                active.setBtyp(isFri);
                active.setDes(cc.getTxt());
                active.setTim(cc.getTim());
                active.setTimstr(TimUtil.formatTimeToStr(cc.getTim()));
                activeDao.save(active);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            quit();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 当退出聊天内容页面时更新“新消息”数量
     * <p>
     * 一、编辑中数据显示。如果，退出后，又有新的未读消息：标记有信息未读，此处显示也显示新信息。
     * 二、未读信息，显示。
     * 三、退出当前聊天时，没有未读信息，不显示任何信息。除非有未发送内容。
     */
    private void quit() {
        quitEditeMsg();
        quitActive();
    }
}

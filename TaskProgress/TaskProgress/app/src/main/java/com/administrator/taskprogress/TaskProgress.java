package com.administrator.taskprogress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;


public class TaskProgress extends Activity {

    SQLiteDatabase db;

    Button bn = null;

    SimpleCursorAdapter simple_cursor_adapter;

    ListView listView;

    TextView emptyText;

    String title, task_total, task_done, percent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        //创建或打开数据库（此处需要使用绝对路径）
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "my.db3", null);

        listView = (ListView)findViewById(R.id.show);

        bn = (Button)findViewById(R.id.add);

        emptyText = (TextView)findViewById(R.id.empty_text);

//        //headerDivider
//        listView.addHeaderView(new View(this), null, true);

//        //FooterDivider
//        listView.addFooterView(new View(this), null, true);

        //set emptyView
        listView.setEmptyView(emptyText);


        try{
            //查询数据
            Cursor cursor = queryAllData(db);

            inflateList(cursor);

        }catch (SQLiteException se){
            //执行DDL创建数据表
            db.execSQL("create table task_manager(_id integer primary key autoincrement,"
                    + " title varchar(255),"
                    + " task_done varchar(50),"
                    + " task_total varchar(50),"
                    + " percent varchar(50),"
                    + " date_time integer)");

            //查询数据
            Cursor cursor = queryAllData(db);

            inflateList(cursor);
        }


        //绑定按钮单击事件
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //定义一个AlertDialog.Builder对象
                final Builder builder = new Builder(TaskProgress.this);

                //设置对话框图标
                builder.setIcon(R.drawable.n);
                //设置对话框标题
                //builder.setTitle("创建新任务");
                //通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                final View n_view = LayoutInflater.from(TaskProgress.this).inflate(R.layout.n_title, null);
                builder.setCustomTitle(n_view);
                //设置对话框显示的内容

                //通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                final View new_view = LayoutInflater.from(TaskProgress.this).inflate(R.layout.add, null);
                builder.setView(new_view);

                builder.setPositiveButton("确定", null);

                //为对话框添加取消按钮
                builder.setNegativeButton(
                        "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        }
                );


                final AlertDialog new_alert = builder.create();
                new_alert.show();

                new_alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //获取用户输入
                        //Dialog上边的控件要加入view做限定，否则会造成空指针的问题
                        title = ((EditText) new_view.findViewById(R.id.title)).getText().toString();
                        task_done = ((EditText) new_view.findViewById(R.id.task_done)).getText().toString();
                        task_total = ((EditText) new_view.findViewById(R.id.task_total)).getText().toString();

                        float result;

                        try{
                            result = Float.parseFloat(task_done) / Float.parseFloat(task_total);
                        }catch(NumberFormatException nfe){
                            Toast toast = Toast.makeText(
                                    TaskProgress.this,
                                    "请输入正确的任务参数！",
                                    Toast.LENGTH_SHORT
                            );
                            toast.show();

                            return;
                        }

                        if(Float.parseFloat(task_total) == 0.0 || result > 1.0){
                            Toast toast = Toast.makeText(
                                    TaskProgress.this,
                                    "请输入正确的任务参数！",
                                    Toast.LENGTH_SHORT
                            );
                            toast.show();

                            return;
                        }

                        //关闭对话框
                        new_alert.dismiss();

                        DecimalFormat df = new DecimalFormat("0.0");
                        percent = df.format(result * 100);


                        Long dateInMillis = Long.parseLong(String.valueOf(Calendar.getInstance().getTimeInMillis()));


                        //执行insert语句插入数据
                        insertData(db, title, task_done, task_total, percent, dateInMillis);

                        //查询数据
                        Cursor new_cursor = queryAllData(db);

                        simple_cursor_adapter.swapCursor(new_cursor);
                        simple_cursor_adapter.notifyDataSetChanged();


                        if (listView != null && listView.getCount() > 0) {
                            listView.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (listView.getFirstVisiblePosition() > 20) {
                                        listView.setSelection(20);
                                    }
                                    listView.smoothScrollToPositionFromTop(0, 0, 250);
                                }
                            });
                        }


                    }
                });

//                //为对话框添加确定按钮
//                builder.setPositiveButton(
//                        "确定",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                //获取用户输入
//                                //Dialog上边的控件要加入view做限定，否则会造成空指针的问题
//                                title = ((EditText) new_view.findViewById(R.id.title)).getText().toString();
//                                task_done = ((EditText) new_view.findViewById(R.id.task_done)).getText().toString();
//                                task_total = ((EditText) new_view.findViewById(R.id.task_total)).getText().toString();
//
//                                float result = Float.parseFloat(task_done) / Float.parseFloat(task_total);
//
//                                DecimalFormat df = new DecimalFormat("0.0");
//
//                                percent = df.format(result * 100);
//
//
//                                //执行insert语句插入数据
//                                insertData(db, title, task_done, task_total, percent);
//
//                                //查询数据
//                                Cursor new_cursor = queryAllData(db);
//
//                                simple_cursor_adapter.swapCursor(new_cursor);
//                                simple_cursor_adapter.notifyDataSetChanged();
//
//                            }
//                        }
//                );
//
//                //为对话框添加取消按钮
//                builder.setNegativeButton(
//                        "取消",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //do nothing
//                            }
//                        }
//                );
//
//                //创建并显示对话框
//                builder.create().show();
            }
        });



        //绑定列表项单击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

//                //保存当前第一个可见的item的索引和偏移量
//                final int index = listView.getFirstVisiblePosition();
//                View v = listView.getChildAt(0);
//                final int top = v == null ? 0 : v.getTop();


                //定义一个AlertDialog.Builder对象
                final Builder builder = new Builder(TaskProgress.this);

                //设置对话框图标
                builder.setIcon(R.drawable.u);
                //设置对话框标题
                //builder.setTitle("更新任务状态");
                //通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                final View u_view = LayoutInflater.from(TaskProgress.this).inflate(R.layout.u_title, null);
                builder.setCustomTitle(u_view);


                //设置对话框显示的内容

                //通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                final View update_view = LayoutInflater.from(TaskProgress.this).inflate(R.layout.update, null);

                builder.setView(update_view);


                //从ListView获取数据
                final Cursor list_cursor = (Cursor)parent.getItemAtPosition(position);

                //the id in database???
                final long row_id = id;

                final String r_title = list_cursor.getString(list_cursor.getColumnIndex("title"));
                final String r_task_total = list_cursor.getString(list_cursor.getColumnIndex("task_total"));
                final String r_task_done = list_cursor.getString(list_cursor.getColumnIndex("task_done"));

                //Dialog上边的控件要加入view做限定，否则会造成空指针的问题
                ((EditText)update_view.findViewById(R.id.title)).setText(r_title);
                ((EditText)update_view.findViewById(R.id.task_done)).setText(r_task_done);
                ((EditText)update_view.findViewById(R.id.task_total)).setText(r_task_total);


                builder.setPositiveButton("确定", null);

                //为对话框添加取消按钮
                builder.setNegativeButton(
                        "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        }
                );

                final AlertDialog update_alert = builder.create();
                update_alert.show();


                update_alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        title = ((EditText)update_view.findViewById(R.id.title)).getText().toString();
                        task_done = ((EditText)update_view.findViewById(R.id.task_done)).getText().toString();
                        task_total = ((EditText)update_view.findViewById(R.id.task_total)).getText().toString();

                        float result;

                        try{
                            result = Float.parseFloat(task_done) / Float.parseFloat(task_total);
                        }catch(NumberFormatException nfe){
                            Toast toast = Toast.makeText(
                                    TaskProgress.this,
                                    "请输入正确的任务参数！",
                                    Toast.LENGTH_SHORT
                            );
                            toast.show();

                            return;
                        }

                        if(Float.parseFloat(task_total) == 0.0 || result > 1.0){
                            Toast toast = Toast.makeText(
                                    TaskProgress.this,
                                    "请输入正确的任务参数！",
                                    Toast.LENGTH_SHORT
                            );
                            toast.show();

                            return;
                        }

                        //关闭对话框
                        update_alert.dismiss();

                        DecimalFormat df = new DecimalFormat("0.0");

                        percent = df.format(result * 100);


                        Long dateInMillis = Long.parseLong(String.valueOf(Calendar.getInstance().getTimeInMillis()));

                        updateData(db, row_id, title, task_done, task_total, percent, dateInMillis);


                        //查询数据
                        Cursor new_cursor = queryAllData(db);
                        simple_cursor_adapter.swapCursor(new_cursor);
                        simple_cursor_adapter.notifyDataSetChanged();

//                        //根据上次保存的索引和偏移量恢复上次的位置
//                        listView.setSelectionFromTop(index, top);


                        if (listView != null && listView.getCount() > 0) {
                            listView.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (listView.getFirstVisiblePosition() > 20) {
                                        listView.setSelection(20);
                                    }
                                    listView.smoothScrollToPositionFromTop(0, 0, 250);
                                }
                            });
                        }

                    }
                });

            }
        });



        //绑定列表项长按事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //定义一个AlertDialog.Builder对象
                final Builder builder = new Builder(TaskProgress.this);

                //设置对话框图标
                builder.setIcon(R.drawable.d);
                //设置对话框标题
                //builder.setTitle("删除任务");
                //通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                final View d_view = LayoutInflater.from(TaskProgress.this).inflate(R.layout.d_title, null);
                builder.setCustomTitle(d_view);

                //设置对话框显示的内容
                builder.setMessage("确定要删除任务吗？");

                //从ListView获取数据
                //final Cursor list_cursor = (Cursor)parent.getItemAtPosition(position);

                //the id in database???
                final long row_id = id;

                //为对话框添加确定按钮
                builder.setPositiveButton(
                        "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deleteData(db, row_id);

                                //查询数据
                                Cursor new_cursor = queryAllData(db);
                                simple_cursor_adapter.swapCursor(new_cursor);
                                simple_cursor_adapter.notifyDataSetChanged();

                            }
                        }
                );

                //为对话框添加取消按钮
                builder.setNegativeButton(
                        "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        }
                );

                //创建并显示对话框
                builder.create().show();

                return true;
            }
        });


    }


    //插入新数据
    private void insertData(SQLiteDatabase db, String title, String task_done, String task_total, String percent, Long date_time){

        Object[] objects = new Object[]{title, task_done, task_total, percent, date_time};

        //执行插入语句
        db.execSQL("insert into task_manager values(null, ?, ?, ?, ?, ?)", objects);

    }

    //删除数据
    private void deleteData(SQLiteDatabase db, long id){
        //执行删除语句
        db.execSQL("DELETE FROM task_manager WHERE _id = " + id);
    }

    //更新数据
    private void updateData(SQLiteDatabase db, long id, String title, String task_done, String task_total, String percent, Long date_time){

        Object[] objects = new Object[]{title, task_done, task_total, percent, date_time};
        //System.out.println("id: " + id);
        //执行更新语句
        db.execSQL("UPDATE task_manager SET title = ?, task_done = ?, task_total = ?, percent = ?, date_time = ? WHERE _id = " + id, objects);

    }

    //查询全部数据
    private Cursor queryAllData(SQLiteDatabase db){
        //执行查询
        //Cursor cursor = db.rawQuery("select * from task_manager order by _id desc", null);
        Cursor cursor = db.rawQuery("select * from task_manager order by date_time desc", null);
        return cursor;
    }


    //查询部分数据
    private Cursor querySingleData(SQLiteDatabase db, long id){
        Cursor cursor = db.rawQuery("select * from task_manager where _id = " + id, null);
        return cursor;
    }



    //显示全部数据
    private void inflateList(Cursor cursor){

        //填充SimpleCursorAdapter
        simple_cursor_adapter = new SimpleCursorAdapter(
                TaskProgress.this,
                R.layout.line,
                cursor,
                new String[]{"title", "percent", "percent", "task_done", "task_total"},
                new int[]{R.id.task_title, R.id.pb, R.id.result, R.id.done, R.id.total}
        );

        simple_cursor_adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

                //System.out.println("inside setViewValue()!!!");

                if(view.getId() == R.id.total){

                    int totalIndex = cursor.getColumnIndex("task_total");
                    String total = cursor.getString(totalIndex);
                    ((TextView) view).setText("/" + total);

                    return true;

                }
                else if(view.getId() == R.id.result){

                    int percentIndex = cursor.getColumnIndex("percent");
                    String percent = cursor.getString(percentIndex);
                    ((TextView) view).setText("已完成：" + percent + "%");

                    //System.out.println("已完成：" + percent + "%");

                    return true;

                }else if(view.getId() == R.id.pb){

                    int percentIndex = cursor.getColumnIndex("percent");
                    float percent = cursor.getFloat(percentIndex);
                    int setValue = (int)(percent * 10);
                    ((ProgressBar) view).setProgress(setValue);

                    //System.out.println("setValue: " + setValue);

                    return true;
                }

                return false;
            }
        });

        //显示数据
        listView.setAdapter(simple_cursor_adapter);
    }

    //关闭数据库！！！
    @Override
    protected void onDestroy(){

        super.onDestroy();

        //退出程序时，关闭SQLiteDatabase
        if(db != null && db.isOpen()){
            db.close();
        }

    }


}

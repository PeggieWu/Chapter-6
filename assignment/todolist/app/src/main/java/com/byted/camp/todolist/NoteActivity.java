package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.byted.camp.todolist.db.TodoContract.*;

public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private Button addBtn;


    private static final String TAG = "NoteActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.take_a_note);

        editText = findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }

        addBtn = findViewById(R.id.btn_add);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence content = editText.getText();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(NoteActivity.this,
                            "No content to add", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean succeed = saveNote2Database(content.toString().trim());
                if (succeed) {
                    Toast.makeText(NoteActivity.this,
                            "Note added", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                } else {
                    Toast.makeText(NoteActivity.this,
                            "Error", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean saveNote2Database(String content) {
        // TODO 插入一条新数据，返回是否插入成功
        TodoDbHelper dbHelper = new TodoDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String date_str = ft.format(date);

        ContentValues values = new ContentValues();

        values.put(TodoEntry.COLUMN_NAME_CONTENT,content);
        values.put(TodoEntry.COLUMN_NAME_STATE,"TODO");
        values.put(TodoEntry.COLUMN_NAME_DATE,date_str);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TodoEntry.TABLE_NAME, null, values);
        Log.i(TAG, "perform add data, result:" + newRowId);
        if (newRowId >0)
            return true;
        else
            return false;

    }
}

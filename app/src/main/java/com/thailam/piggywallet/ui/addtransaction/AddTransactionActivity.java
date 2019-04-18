package com.thailam.piggywallet.ui.addtransaction;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.thailam.piggywallet.R;
import com.thailam.piggywallet.data.model.Category;
import com.thailam.piggywallet.data.model.Transaction;
import com.thailam.piggywallet.data.source.TransactionDataSource;
import com.thailam.piggywallet.data.source.TransactionRepository;
import com.thailam.piggywallet.data.source.local.TransactionLocalDataSource;
import com.thailam.piggywallet.ui.category.CategoryDialog;
import com.thailam.piggywallet.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener, CategoryDialog.OnCategoryChosen, AddTransactionContract.View {

    private AddTransactionContract.Presenter mPresenter;
    private DatePickerDialog mDatePickerDialog;
    private Category mCategory;
    private EditText mEditTextNote;
    private EditText mEditTextAmount;
    private Button mBtnChooseCategory, mBtnChooseDate;
    private long mDate;

    public static Intent getIntent(Context context) {
        return new Intent(context, AddTransactionActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        initPresenter();
        initToolbar();
        initViews();
        initDatePicker();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = String.format(Locale.US, "%d-%d-%d", dayOfMonth, month, year);
        String errMsg = getResources().getString(R.string.add_transaction_choose_date_fail);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-YY");
        try {
            Date d = format.parse(date);
            mDate = d.getTime();
            mBtnChooseDate.setText(date);
        } catch (ParseException e) {
            Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onGetCategory(Category category) {
        mCategory = category;
        mBtnChooseCategory.setText(category.getName());
    }

    @Override
    public void initPresenter() {
        if (mPresenter == null) {
            TransactionDataSource source = TransactionLocalDataSource.getInstance(getApplicationContext());
            TransactionRepository repo = TransactionRepository.getInstance(source);
            mPresenter = new AddTransactionPresenter(this, repo);
        }
    }

    @Override
    public void showSuccess() {
        String msg = getResources().getString(R.string.add_transaction_success);
        finish(); // move back to prev screen
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String str) {
        String errMsg = str == null ? Constants.UNKNOWN_ERROR : str;
        Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorPrompt(String errCode) {
        if (errCode.equals(Constants.ERR_CODE_MISSING_AMOUNT)) {
            String errMsg = getResources().getString(R.string.add_transaction_missing_amount);
            Toast.makeText(this,errMsg,Toast.LENGTH_SHORT).show();
        } else if (errCode.equals(Constants.ERR_CODE_MISSING_CATEGORY)) {
            String errMsg=getResources().getString(R.string.add_transaction_missing_category);
            Toast.makeText(this,errMsg,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_add_transaction_category:
                CustomDialog dialog = new CustomDialog(this);
                dialog.show();
                break;
            case R.id.button_add_transaction_date:
                mDatePickerDialog.show();
                break;
            case R.id.button_add_transaction_save:
                handleSaveTransaction();
                break;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_add_transaction);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePickerDialog = new DatePickerDialog(this, AddTransactionActivity.this,
                year, month, dayOfMonth);
    }

    private void initViews() {
        mEditTextAmount = findViewById(R.id.edit_text_add_transaction_amount);
        mEditTextNote = findViewById(R.id.edit_text_add_transaction_note);
        mBtnChooseCategory = findViewById(R.id.button_add_transaction_category);
        mBtnChooseDate = findViewById(R.id.button_add_transaction_date);
    }

    private void handleSaveTransaction() {
        mPresenter.saveTransaction(mEditTextNote, mEditTextAmount, mCategory, mDate);
    }
}

package codepath.apps.tipcalculator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {

	protected EditText etCheckAmount;
	protected SeekBar sbTipPct;
	protected TextView tvTipPctVal;
	protected CheckBox cbRoundAmount;
	protected TextView tvTotalAmountVal;
	protected TextView tvTipAmountVal;
	protected TextView tvErrorMessage;
	
	final int MAX_TIP_PCT = 30;
	final int DEFAULT_TIP_PCT = 15;

	final DecimalFormat decfmt = new DecimalFormat("0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		etCheckAmount = (EditText)findViewById(R.id.etCheckAmount);
		sbTipPct = (SeekBar)findViewById(R.id.sbTipPct);
		tvTipPctVal = (TextView)findViewById(R.id.tvTipPctVal);
		cbRoundAmount = (CheckBox)findViewById(R.id.cbRoundAmount);
		tvTotalAmountVal = (TextView)findViewById(R.id.tvTotalAmountVal);
		tvTipAmountVal = (TextView)findViewById(R.id.tvTipAmountVal);
		tvErrorMessage = (TextView)findViewById(R.id.tvErrorMessage);

		initCheckAmountTextView();
		initTipPctSeekBar();
		initRoundAmountCheckBox();
	}

	protected void initCheckAmountTextView() {
		etCheckAmount.setTextColor(Color.BLACK);

		etCheckAmount.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				computeValues();
			}
		});
	}

	protected void initTipPctSeekBar() {
		sbTipPct.setMax(MAX_TIP_PCT);
		sbTipPct.incrementProgressBy(1);
		sbTipPct.setProgress(DEFAULT_TIP_PCT);

		final NumberFormat pctfmt = NumberFormat.getPercentInstance();
		tvTipPctVal.setText(pctfmt.format((double)DEFAULT_TIP_PCT / 100));

		sbTipPct.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}

		    @Override
		    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		    	tvTipPctVal.setText(pctfmt.format((double)progress / 100));
		    	computeValues();
		    }
		});
	}

	protected void initRoundAmountCheckBox() {
		cbRoundAmount.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				computeValues();
			}
		});
	}

	protected void computeValues() {
		double checkAmount;

		etCheckAmount.setTextColor(Color.RED);
		tvTotalAmountVal.setText("");
		tvTipAmountVal.setText("");
		tvErrorMessage.setText("");

		if (etCheckAmount.getText().toString().trim().equals("")) {
			return;
		}

		try {
			checkAmount = decfmt.parse(etCheckAmount.getText().toString()).doubleValue();
		} catch (ParseException e) {
			tvErrorMessage.setText(R.string.invalid_number);
			return;
		}

		if (checkAmount <= 0) {
			tvErrorMessage.setText(R.string.negative_number);
			return;
		}

		etCheckAmount.setTextColor(Color.BLACK);
		tvErrorMessage.setText("");

		double tipPct = (double) sbTipPct.getProgress() / 100;
		double tipAmount = checkAmount * tipPct;
		double totalAmount = checkAmount + tipAmount;

		if (cbRoundAmount.isChecked()) {
			totalAmount = Math.ceil(totalAmount);
			tipAmount = totalAmount - checkAmount;
		}

		NumberFormat curfmt = NumberFormat.getCurrencyInstance();

		tvTotalAmountVal.setText(curfmt.format(totalAmount));
		tvTipAmountVal.setText(curfmt.format(tipAmount));
	}
}

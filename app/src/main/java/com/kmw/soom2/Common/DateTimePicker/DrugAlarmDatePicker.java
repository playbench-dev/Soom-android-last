package com.kmw.soom2.Common.DateTimePicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.kmw.soom2.Common.DateTimePicker.WheelAmPmPicker;
import com.kmw.soom2.Common.DateTimePicker.WheelDayOfMonthPicker;
import com.kmw.soom2.Common.DateTimePicker.WheelDayPicker;
import com.kmw.soom2.Common.DateTimePicker.WheelHourPicker;
import com.kmw.soom2.Common.DateTimePicker.WheelMinutePicker;
import com.kmw.soom2.Common.DateTimePicker.WheelMonthPicker;
import com.kmw.soom2.Common.DateTimePicker.WheelPicker;
import com.kmw.soom2.Common.DateTimePicker.WheelYearPicker;
import com.kmw.soom2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.kmw.soom2.Common.DateTimePicker.DateHelper.getCalendarOfDate;

public class DrugAlarmDatePicker extends LinearLayout {

    public static final boolean IS_CYCLIC_DEFAULT = true;
    public static final boolean IS_CURVED_DEFAULT = false;
    public static final boolean MUST_BE_ON_FUTUR_DEFAULT = false;
    public static final int DELAY_BEFORE_CHECK_PAST = 200;
    private static final int VISIBLE_ITEM_COUNT_DEFAULT = 7;
    private static final int PM_HOUR_ADDITION = 12;

    private static final CharSequence FORMAT_24_HOUR = "EEE d MMM H:mm";
    private static final CharSequence FORMAT_12_HOUR = "EEE d MMM h:mm a";


    @NonNull
    private final WheelYearPicker yearsPicker;

    @NonNull
    private final WheelMonthPicker monthPicker;

    @NonNull
    private final WheelDayOfMonthPicker daysOfMonthPicker;

    @NonNull
    private final WheelDayPicker daysPicker;
    @NonNull
    private final WheelMinutePicker minutesPicker;
    @NonNull
    private final WheelHourPicker hoursPicker;
    @NonNull
    private final WheelAmPmPicker amPmPicker;

    private List<WheelPicker> pickers = new ArrayList<>();

    private List<com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker.OnDateChangedListener> listeners = new ArrayList<>();
    private List<com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker.OnCustomClick> customListeners = new ArrayList<>();
    private List<com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker.OnCancelClick> cancelClicks = new ArrayList<>();

    private View dtSelector;
    private boolean mustBeOnFuture;

    Button btnDone,btnCancel;
    LinearLayout linBtnParent;
    LinearLayout linViewParent;

    @Nullable
    private Date minDate;
    @Nullable
    private Date maxDate;
    @NonNull
    private Date defaultDate;

    private boolean displayYears = false;
    private boolean displayMonth = true;
    private boolean displayDaysOfMonth = false;
    private boolean displayDays = false;
    private boolean displayMinutes = true;
    private boolean displayHours = true;

    private boolean isAmPm;
    private int selectorHeight;

    public DrugAlarmDatePicker(Context context) {
        this(context, null);
    }

    public DrugAlarmDatePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrugAlarmDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        defaultDate = new Date();
        isAmPm = !(DateFormat.is24HourFormat(context));

        inflate(context, R.layout.single_day_picker_drug, this);

        linBtnParent = (LinearLayout)findViewById(R.id.lin_picker_btn_parent);
        linViewParent = (LinearLayout)findViewById(R.id.lin_picker_view_parent);
        btnDone = (Button)findViewById(R.id.btn_picker_done);
        btnCancel = (Button)findViewById(R.id.btn_picker_cancel);
        yearsPicker = (WheelYearPicker) findViewById(R.id.yearPicker);
        monthPicker = (WheelMonthPicker) findViewById(R.id.monthPicker);
        daysOfMonthPicker = (WheelDayOfMonthPicker) findViewById(R.id.daysOfMonthPicker);
        daysPicker = (WheelDayPicker) findViewById(R.id.daysPicker);
        minutesPicker = (WheelMinutePicker) findViewById(R.id.minutesPicker);
        hoursPicker = (WheelHourPicker) findViewById(R.id.hoursPicker);
        amPmPicker = (WheelAmPmPicker) findViewById(R.id.amPmPicker);
        dtSelector = findViewById(R.id.dtSelector);

        pickers.addAll(Arrays.asList(
                daysPicker,
                minutesPicker,
                hoursPicker,
                amPmPicker,
                daysOfMonthPicker,
                monthPicker,
                yearsPicker
        ));

        init(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        yearsPicker.setOnYearSelectedListener(new WheelYearPicker.OnYearSelectedListener() {
            @Override
            public void onYearSelected(WheelYearPicker picker, int position, int year) {
                updateListener();
                checkMinMaxDate(picker);
            }
        });

        monthPicker.setOnMonthSelectedListener(new WheelMonthPicker.MonthSelectedListener() {
            @Override
            public void onMonthSelected(WheelMonthPicker picker, int monthIndex, String monthName) {
                updateListener();
                checkMinMaxDate(picker);

                if (displayDaysOfMonth) {
                    updateDaysOfMonth(0);
                }
            }
        });

        daysOfMonthPicker
                .setDayOfMonthSelectedListener(new WheelDayOfMonthPicker.DayOfMonthSelectedListener() {
                    @Override
                    public void onDayOfMonthSelected(WheelDayOfMonthPicker picker, int dayIndex) {
                        updateListener();
                        checkMinMaxDate(picker);
                    }
                });

        daysOfMonthPicker
                .setOnFinishedLoopListener(new WheelDayOfMonthPicker.FinishedLoopListener() {
                    @Override
                    public void onFinishedLoop(WheelDayOfMonthPicker picker) {
                        if (displayMonth) {
                            monthPicker.scrollTo(monthPicker.getCurrentItemPosition() + 1);
                            updateDaysOfMonth(0);
                        }
                    }
                });

        daysPicker
                .setOnDaySelectedListener(new WheelDayPicker.OnDaySelectedListener() {
                    @Override
                    public void onDaySelected(WheelDayPicker picker, int position, String name, Date date) {
                        updateListener();
                        checkMinMaxDate(picker);
                    }
                });

        minutesPicker
                .setOnMinuteChangedListener(new WheelMinutePicker.OnMinuteChangedListener() {
                    @Override
                    public void onMinuteChanged(WheelMinutePicker picker, int minutes) {
                        updateListener();
                        checkMinMaxDate(picker);
                    }
                })
                .setOnFinishedLoopListener(new WheelMinutePicker.OnFinishedLoopListener() {
                    @Override
                    public void onFinishedLoop(WheelMinutePicker picker) {
                        hoursPicker.scrollTo(hoursPicker.getCurrentItemPosition() + 1);
                    }
                });

        hoursPicker
                .setOnFinishedLoopListener(new WheelHourPicker.FinishedLoopListener() {
                    @Override
                    public void onFinishedLoop(WheelHourPicker picker) {
                        daysPicker.scrollTo(daysPicker.getCurrentItemPosition() + 1);
                    }
                })
                .setHourChangedListener(new WheelHourPicker.OnHourChangedListener() {
                    @Override
                    public void onHourChanged(WheelHourPicker picker, int hour) {
                        updateListener();
                        checkMinMaxDate(picker);
                    }
                });

        amPmPicker
                .setAmPmListener(new WheelAmPmPicker.AmPmListener() {
                    @Override
                    public void onAmPmChanged(WheelAmPmPicker picker, boolean isAm) {
                        updateListener();
                        checkMinMaxDate(picker);
                    }
                });

        btnDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickTest();
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelTest();
            }
        });

        setDefaultDate(this.defaultDate); //update displayed date
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (WheelPicker picker : pickers) {
            picker.setEnabled(enabled);
        }
    }

    public void setDisplayYears(boolean displayYears) {
        this.displayYears = displayYears;
        yearsPicker.setVisibility(displayYears ? VISIBLE : GONE);
    }

    public void setDisplayMonths(boolean displayMonths) {
        this.displayMonth = displayMonths;
        monthPicker.setVisibility(displayMonths ? VISIBLE : GONE);
        checkSettings();
    }

    public void setDisplayDaysOfMonth(boolean displayDaysOfMonth) {
        this.displayDaysOfMonth = displayDaysOfMonth;
        daysOfMonthPicker.setVisibility(displayDaysOfMonth ? VISIBLE : GONE);

        if (displayDaysOfMonth) {
            updateDaysOfMonth(1);
        }
        checkSettings();
    }

    public void setDisplayDays(boolean displayDays) {
        this.displayDays = displayDays;
        daysPicker.setVisibility(displayDays ? VISIBLE : GONE);
        checkSettings();
    }

    public void setBtnVisible(boolean visibliliy){
        linBtnParent.setVisibility(GONE);
        linViewParent.setVisibility(GONE);
    }

    public void setDisplayMinutes(boolean displayMinutes) {
        this.displayMinutes = displayMinutes;
        minutesPicker.setVisibility(displayMinutes ? VISIBLE : GONE);
    }

    public void setDisplayHours(boolean displayHours) {
        this.displayHours = displayHours;
        hoursPicker.setVisibility(displayHours ? VISIBLE : GONE);

        setIsAmPm(this.isAmPm);
        hoursPicker.setIsAmPm(isAmPm);
    }

    public void setDisplayMonthNumbers(boolean displayMonthNumbers) {
        this.monthPicker.setDisplayMonthNumbers(displayMonthNumbers);
        this.monthPicker.updateAdapter();
    }

    public void setTodayText(String todayText) {
        if (todayText != null && !todayText.isEmpty()) {
            daysPicker.setTodayText(todayText);
        }
    }

    public void setCurved(boolean curved) {
        for (WheelPicker picker : pickers) {
            picker.setCurved(curved);
        }
    }

    public void setCyclic(boolean cyclic) {
        for (WheelPicker picker : pickers) {
            picker.setCyclic(cyclic);
        }
    }

    public void setTextSize(int textSize) {
        for (WheelPicker picker : pickers) {
            picker.setItemTextSize(textSize);
        }
    }

    public void setSelectedTextColor(int selectedTextColor) {
        for (WheelPicker picker : pickers) {
            picker.setSelectedItemTextColor(selectedTextColor);
        }
    }

    public void setTextColor(int textColor) {
        for (WheelPicker picker : pickers) {
            picker.setItemTextColor(textColor);
        }
    }

    public void setSelectorColor(int selectorColor) {
        dtSelector.setBackgroundColor(selectorColor);
    }

    public void setSelectorHeight(int selectorHeight) {
        final ViewGroup.LayoutParams dtSelectorLayoutParams = dtSelector.getLayoutParams();
        dtSelectorLayoutParams.height = selectorHeight;
        dtSelector.setLayoutParams(dtSelectorLayoutParams);
    }

    public void setVisibleItemCount(int visibleItemCount) {
        for (WheelPicker picker : pickers) {
            picker.setVisibleItemCount(visibleItemCount);
        }
    }

    public void setIsAmPm(boolean isAmPm) {
        this.isAmPm = isAmPm;

        amPmPicker.setVisibility((isAmPm && displayHours) ? VISIBLE : GONE);
        hoursPicker.setIsAmPm(isAmPm);
    }

    public void setDayFormatter(SimpleDateFormat simpleDateFormat) {
        if (simpleDateFormat != null) {
            this.daysPicker.setDayFormatter(simpleDateFormat);
        }
    }

    public boolean isAmPm() {
        return isAmPm;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
        setMinYear();
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
        setMinYear();
    }

    private void checkMinMaxDate(final WheelPicker picker) {
        checkBeforeMinDate(picker);
        checkAfterMaxDate(picker);
    }

    private void checkBeforeMinDate(final WheelPicker picker) {
//        picker.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (minDate != null && isBeforeMinDate(getDate())) {
//                    for (WheelPicker p : pickers) {
//                        p.scrollTo(p.findIndexOfDate(minDate));
//                    }
//                }
//            }
//        }, DELAY_BEFORE_CHECK_PAST);
    }

    private void checkAfterMaxDate(final WheelPicker picker) {
        picker.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (maxDate != null && isAfterMaxDate(getDate())) {
                    for (WheelPicker p : pickers) {
                        p.scrollTo(p.findIndexOfDate(maxDate));
                    }
                }
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    private boolean isBeforeMinDate(Date date) {
        return getCalendarOfDate(date).before(getCalendarOfDate(minDate));
    }

    private boolean isAfterMaxDate(Date date) {
        return getCalendarOfDate(date).after(getCalendarOfDate(maxDate));
    }

    public void addOnDateChangedListener(com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker.OnDateChangedListener listener) {
        this.listeners.add(listener);
    }

    public void clickDateChange(com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker.OnCustomClick listener){
        this.customListeners.add(listener);
    }

    public void clickCancelDialog(com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker.OnCancelClick listener){
        this.cancelClicks.add(listener);
    }

    public void removeOnDateChangedListener(com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker.OnDateChangedListener listener) {
        this.listeners.remove(listener);
    }

    public void checkPickersMinMax() {
        for (WheelPicker picker : pickers) {
            checkMinMaxDate(picker);
        }
    }

    public Date getDate() {
        int hour = hoursPicker.getCurrentHour();
        if (isAmPm && amPmPicker.isPm()) {
            hour += PM_HOUR_ADDITION;
        }
        final int minute = minutesPicker.getCurrentMinute();

        final Calendar calendar = Calendar.getInstance();

        if (displayDays) {
            final Date dayDate = daysPicker.getCurrentDate();
            calendar.setTime(dayDate);
            if (displayMonth) {
                calendar.set(Calendar.MONTH, monthPicker.getCurrentMonth());
            }

            if (displayYears) {
                calendar.set(Calendar.YEAR, yearsPicker.getCurrentYear());
            }

            if (displayDaysOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, daysOfMonthPicker.getCurrentDay() + 1);
            }

            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,minute);
        } else {
            if (displayMonth) {
                calendar.set(Calendar.MONTH, monthPicker.getCurrentMonth());
            }

            if (displayYears) {
                calendar.set(Calendar.YEAR, yearsPicker.getCurrentYear());
            }

            if (displayDaysOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, daysOfMonthPicker.getCurrentDay() + 1);
            }
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,minute);
        }

        return calendar.getTime();
    }

    public void setStepMinutes(int minutesStep) {
        minutesPicker.setStepMinutes(minutesStep);
    }

    public void setHoursStep(int hoursStep) {
        hoursPicker.setHoursStep(hoursStep);
    }

    public void setDefaultDate(Date date) {
        if (date != null) {
            this.defaultDate = date;

            for (WheelPicker picker : pickers) {
                picker.setDefaultDate(defaultDate);
            }
        }
    }

    public void selectDate(Calendar calendar) {
        if (calendar == null) {
            return;
        }

        final Date date = calendar.getTime();
        for (WheelPicker picker : pickers) {
            picker.selectDate(date);
        }

        if (displayDaysOfMonth) {
            updateDaysOfMonth(0);
        }
    }

    private void updateListener() {
        final Date date = getDate();
        final CharSequence format = isAmPm ? FORMAT_12_HOUR : FORMAT_24_HOUR;
        final String displayed = DateFormat.format(format, date).toString();
        for (com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker.OnDateChangedListener listener : listeners) {
            listener.onDateChanged(displayed, date);
        }

    }

    private void onClickTest(){
        final Date date = getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        final CharSequence format = isAmPm ? FORMAT_12_HOUR : FORMAT_24_HOUR;
        final String displayed = DateFormat.format(format, date).toString();
        for (com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker.OnCustomClick listener : customListeners) {
            listener.onDateChanged(displayed, date);
        }
    }

    private void onCancelTest(){
        for (com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker.OnCancelClick listener : cancelClicks){
            listener.onDialogCancel();
        }
    }

    private void updateDaysOfMonth(int i) {
        final Date date = getDate();
        Calendar calendar = Calendar.getInstance();
        if (i == 1){
            calendar.setTime(new Date(System.currentTimeMillis()));
        }else{
            calendar.setTime(date);
        }

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        daysOfMonthPicker.setDaysInMonth(daysInMonth);
        daysOfMonthPicker.updateAdapter();
    }

    public void setMustBeOnFuture(boolean mustBeOnFuture) {
        this.mustBeOnFuture = mustBeOnFuture;
        if (mustBeOnFuture) {
            minDate = Calendar.getInstance().getTime(); //minDate is Today
        }
    }

    public boolean mustBeOnFuture() {
        return mustBeOnFuture;
    }

    private void setMinYear() {
        if (displayYears && this.minDate != null && this.maxDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.minDate);
            yearsPicker.setMinYear(calendar.get(Calendar.YEAR));
            calendar.setTime(this.maxDate);
            yearsPicker.setMaxYear(calendar.get(Calendar.YEAR));
        }
    }

    private void checkSettings() {
        if (displayDays && (displayDaysOfMonth || displayMonth)) {
            throw new IllegalArgumentException("You can either display days with months or days and months separately");
        }
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SingleDateAndTimePicker);

        final Resources resources = getResources();
        setTodayText(a.getString(R.styleable.SingleDateAndTimePicker_picker_todayText));
        setTextColor(a.getColor(R.styleable.SingleDateAndTimePicker_picker_textColor, ContextCompat.getColor(context, R.color.picker_default_text_color)));
        setSelectedTextColor(a.getColor(R.styleable.SingleDateAndTimePicker_picker_selectedTextColor, ContextCompat.getColor(context, R.color.picker_default_selected_text_color)));
        setSelectorColor(a.getColor(R.styleable.SingleDateAndTimePicker_picker_selectorColor, ContextCompat.getColor(context, R.color.picker_default_selector_color)));
        setSelectorHeight(a.getDimensionPixelSize(R.styleable.SingleDateAndTimePicker_picker_selectorHeight, 100));
        setTextSize(80);
        setCurved(a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_curved, IS_CURVED_DEFAULT));
        setCyclic(a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_cyclic, IS_CYCLIC_DEFAULT));
        setMustBeOnFuture(a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_mustBeOnFuture, MUST_BE_ON_FUTUR_DEFAULT));
        setVisibleItemCount(a.getInt(R.styleable.SingleDateAndTimePicker_picker_visibleItemCount, VISIBLE_ITEM_COUNT_DEFAULT));

        setDisplayDays(a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_displayDays, displayDays));
        setDisplayMinutes(a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_displayMinutes, displayMinutes));
        setDisplayHours(a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_displayHours, displayHours));
        setDisplayMonths(a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_displayMonth, displayMonth));
        setDisplayYears(a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_displayYears, displayYears));
        setDisplayDaysOfMonth(a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_displayDaysOfMonth, displayDaysOfMonth));
        setDisplayMonthNumbers(a.getBoolean(R.styleable.SingleDateAndTimePicker_picker_displayMonthNumbers, monthPicker.displayMonthNumbers()));

        checkSettings();
        setMinYear();

        a.recycle();

        if (displayDaysOfMonth) {
            updateDaysOfMonth(0);
        }
    }

    public interface OnDateChangedListener {
        void onDateChanged(String displayed, Date date);
    }

    public interface OnCustomClick{
        void onDateChanged(String displayed, Date date);
    }

    public interface OnCancelClick{
        void onDialogCancel();
    }
}

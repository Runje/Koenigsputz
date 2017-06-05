package com.apps.koenig.thomas.koenigsputz.View;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.apps.koenig.thomas.koenigsputz.Koenigsputz;
import com.apps.koenig.thomas.koenigsputz.R;
import com.example.communication.model.CleanTask;
import com.example.communication.model.Frequency;

import org.joda.time.DateTime;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAddTaskListener} interface
 * to handle interaction events.
 * Use the {@link AddTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTaskFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnAddTaskListener mListener;
    private Spinner spinnerResponsible;
    private EditText editFirstDate;
    private NumberPicker numberPicker;
    private CheckBox checkBoxOnce;
    private RadioGroup radioGroup;

    public AddTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTaskFragment newInstance(String param1, String param2) {
        AddTaskFragment fragment = new AddTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        spinnerResponsible = (Spinner) view.findViewById(R.id.spinnerResponsible);
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, mListener.getResponsibleChoices());
        spinnerResponsible.setAdapter(spinnerAdapter);
        spinnerResponsible.invalidate();
        editFirstDate = (EditText) view.findViewById(R.id.editFirstDate);
        editFirstDate.setText(DateTime.now().toString(Koenigsputz.DATE_FORMAT));
        editFirstDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddTask", "Click on first date");
                openSetDateDialog();
            }
        });

        numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(30);
        numberPicker.setValue(1);

        radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup_frequency);
        checkBoxOnce = (CheckBox) view.findViewById(R.id.checkboxOnce);
        checkBoxOnce.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                view.findViewById(R.id.layout_frequency).setVisibility(isChecked ? View.GONE : View.VISIBLE);
                ((TextView) view.findViewById(R.id.text_first_date)).setText(isChecked ? R.string.execution : R.string.first_execution);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddTaskListener) {
            mListener = (OnAddTaskListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddTaskListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void openSetDateDialog() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        final View layout = LayoutInflater.from(getContext()).inflate(R.layout.date_picker, null);
        builder.setView(layout);
        builder.setTitle(R.string.date);
        builder.setNegativeButton(R.string.cancel, null);
        final DatePicker datePicker = (DatePicker) layout.findViewById(R.id.datePicker);
        ViewUtilities.setDateToDatePicker(datePicker, DateTime.now());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                DateTime date = ViewUtilities.getDateFromDatePicker(datePicker);
                editFirstDate.setText(date.toString(Koenigsputz.DATE_FORMAT));
            }
        });

        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }
    public CleanTask getCleanTask() {
        EditText editName = (EditText) getView().findViewById(R.id.editName);
        EditText editDescription = (EditText) getView().findViewById(R.id.editDescription);
        RatingBar ratingBar = (RatingBar) getView().findViewById(R.id.ratingBar);
        EditText editDuration = (EditText) getView().findViewById(R.id.editDuration);


        String name = editName.getText().toString();
        String description = editDescription.getText().toString();
        String responsible = (String) spinnerResponsible.getSelectedItem();
        int difficulty = ratingBar.getNumStars();
        int duration = 0;
        try
        {
            duration = Integer.parseInt(editDuration.getText().toString());
        }
        catch (NumberFormatException e)
        {
            duration = 0;
        }

        DateTime firstDate = Koenigsputz.DATE_FORMAT.parseDateTime(editFirstDate.getText().toString());


        if (!checkBoxOnce.isChecked()) {
            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            View radioButton = radioGroup.findViewById(radioButtonID);
            int idx = radioGroup.indexOfChild(radioButton);

            Frequency frequency = Frequency.indexToFrequency(idx);
            int frequencyNumber = numberPicker.getValue();


            return new CleanTask(name, description, responsible, difficulty, duration, firstDate, frequency, frequencyNumber);
        }
        else
        {
            return new CleanTask(name, description, responsible, difficulty, duration, firstDate);
        }
    }


    public interface OnAddTaskListener {
        String[] getResponsibleChoices();
    }
}

package com.apps.koenig.thomas.koenigsputz.View;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apps.koenig.thomas.koenigsputz.R;
import com.apps.koenig.thomas.koenigsputz.View.RecyclerView.CleanTaskAdapter;
import com.apps.koenig.thomas.koenigsputz.View.RecyclerView.DividerItemDecoration;
import com.apps.koenig.thomas.koenigsputz.View.RecyclerView.HeaderItem;
import com.apps.koenig.thomas.koenigsputz.View.RecyclerView.Item;
import com.apps.koenig.thomas.koenigsputz.View.RecyclerView.ListItem;
import com.apps.koenig.thomas.koenigsputz.dummy.DummyContent.DummyItem;
import com.example.communication.model.CleanTask;
import com.example.communication.model.Frequency;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CleanPlanFragment extends Fragment implements CleanPlanView{
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    public static final String ARG_CLEAN_PLAN = "cleanplan";
    private OnListFragmentInteractionListener mListener;
    private CleanTaskAdapter adapter;
    private ArrayList<ListItem> cleanPlan;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CleanPlanFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CleanPlanFragment newInstance(List<ListItem> cleanPlan) {
        CleanPlanFragment fragment = new CleanPlanFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_CLEAN_PLAN, (ArrayList<? extends Parcelable>) cleanPlan);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.info("OnCreate");
        if (getArguments() != null) {
            cleanPlan = getArguments().getParcelableArrayList(ARG_CLEAN_PLAN);
        } else {
            cleanPlan = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logger.info("OnCreateView");
        View view = inflater.inflate(R.layout.fragment_cleantask, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            /***
             * TEST CODE
             */
            List<ListItem> cleanTaskList = new ArrayList<>();
            /*cleanTaskList.add(new HeaderItem("Today"));
            cleanTaskList.add(new Item(new CleanTask("Bad putzen", "Beide Bäder +  Küche wischen", "Thomas", 3, 30, DateTime.now(), Frequency.weekly, 1)));
            cleanTaskList.add(new Item(new CleanTask("Programmieren", "Putzapp", "Thomas", 1, 30, DateTime.now())));
            cleanTaskList.add(new HeaderItem("Tomorrow"));
            cleanTaskList.add(new Item(new CleanTask("Saugen", "Alle Räume", "Milena", 1, 30, DateTime.now().plusDays(1), Frequency.weekly, 1)));*/
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            adapter = new CleanTaskAdapter(cleanPlan);
            recyclerView.setAdapter(adapter);
        }

        Toast.makeText(getContext(), "Putzplan", Toast.LENGTH_SHORT).show();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showCleanTasks(List<ListItem> cleanTaskList) {
        adapter.setCleanTasks(cleanTaskList);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}

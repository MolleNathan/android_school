package com.example.nathan.schoolmollenathan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nathan on 10/06/2018.
 */

public class ActionBar extends Fragment {
        private static final String TITLE_PARAM = "title";
        private static final String MENU_PARAM = "menu";
        private static final String SCHOOL_PARAM = "school";

        private String title;
        private String menu;
        private School school;

        private OnFragmentInteractionListener mListener;

        public ActionBar() {
            // Required empty public constructor
        }


        public static ActionBar newInstance(String param1, String param2, School param3) {
            ActionBar fragment = new ActionBar();
            Bundle args = new Bundle();
            args.putString(TITLE_PARAM, param1);
            args.putString(MENU_PARAM, param2);
            args.putSerializable(SCHOOL_PARAM, param3);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d("arguments", getArguments().toString());
            if (getArguments() != null) {
                title = getArguments().getString(TITLE_PARAM);
                menu = getArguments().getString(MENU_PARAM);
                school = (School) getArguments().getSerializable(SCHOOL_PARAM);

                if (menu == "ecoleDétail") {
                    setHasOptionsMenu(true);
                }
            }

        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_detail, menu);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_action_bar, container, false);
            TextView fragTitle = view.findViewById(R.id.fragTitle);
            fragTitle.setText(title);

            view.findViewById(R.id.back_button).setOnClickListener(genericOnClickListener);

            ImageButton generalButton = view.findViewById(R.id.general_button);
            generalButton.setOnClickListener(genericOnClickListener);
            view.findViewById(R.id.general_button).setOnClickListener(genericOnClickListener);

            switch (menu) {
                case "map":
                    view.setBackgroundColor(getResources().getColor(R.color.green));
                    generalButton.setImageResource(R.drawable.ic_menu);
                    break;
                case "ecoleList":
                case "ecoleUpdate":
                    view.setBackgroundColor(getResources().getColor(R.color.orange));
                    break;
                case "ecoleDétail":
                    view.setBackgroundColor(getResources().getColor(R.color.orange));
                    generalButton.setImageResource(R.drawable.ic_menu);
                    break;
                case "creation":
                    view.setBackgroundColor(getResources().getColor(R.color.purple));
                    generalButton.setImageResource(R.drawable.ic_delete);
                    break;
                case "config":
                    view.setBackgroundColor(getResources().getColor(R.color.yellow));
                    break;

            }

            return view;
        }


        final View.OnClickListener genericOnClickListener = new View.OnClickListener() {
            public void onClick(final View v) {
                switch (v.getId()) {
                    case R.id.general_button:
                        switch (menu) {
                            case "creation":
                                getActivity().finish();
                                break;
                            case "map":
                                Intent i = new Intent(getContext(), ListActivity.class);
                                startActivity(i);
                                break;
                            case "ecoleDétail":
                                PopupMenu pm = new PopupMenu(getContext(), v);
                                pm.getMenuInflater().inflate(R.menu.menu_detail, pm.getMenu());
                                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.delete:
                                                final SchoolService schoolService = RequestInterface.getInterface(SchoolService.class);
                                                new AlertDialog.Builder(getContext()).setTitle("Suppression")
                                                        .setMessage("Etes vous sûre de vouloir supprimer l’école " + school.getName())
                                                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                schoolService.removeEcole(school.getId()).enqueue(new Callback<ResponseBody>() {
                                                                    @Override
                                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                        getActivity().finish();
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                        Toast.makeText(getContext(), "Erreur", Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
                                                            }
                                                        })
                                                        .setNegativeButton("Non", null)
                                                        .show();

                                                break;
                                            case R.id.update:
                                                Intent i = new Intent(getContext(), UpdateActivity.class);
                                                i.putExtra("school", school);
                                                startActivityForResult(i, 1);
                                                break;
                                            default:
                                        }
                                        return true;
                                    }
                                });
                                pm.show();
                        }
                        break;
                    case R.id.back_button:
                        getActivity().finish();
                        break;
                }
            }
        };

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof OnFragmentInteractionListener) {
                mListener = (OnFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            mListener = null;
        }

        /**
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */
        public interface OnFragmentInteractionListener {
            // TODO: Update argument type and name
            void onFragmentInteraction(String test);
        }
    }



package dadm.scaffold.counter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;


public class ShipSelectionFragment extends BaseFragment implements View.OnClickListener {
    public ShipSelectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ship_selection, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_ship1).setOnClickListener(this);
        view.findViewById(R.id.btn_ship2).setOnClickListener(this);
        view.findViewById(R.id.btn_ship3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = 0;
        switch ((v.getId())){
            case R.id.btn_ship1:
                id = 1;
                break;
            case R.id.btn_ship2:
                id = 2;
                break;
            case R.id.btn_ship3:
                id = 3;
                break;
        }
        //Le pasamos el id de la nave correspondiente
        ((ScaffoldActivity)getActivity()).startGame(id);
    }
}

package dadm.scaffold.counter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;


public class GameOverFragment extends BaseFragment implements View.OnClickListener {
    public GameOverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_over, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_menu).setOnClickListener(this);
        view.findViewById(R.id.btn_restart).setOnClickListener(this);
        view.findViewById(R.id.btn_reselect).setOnClickListener(this);
        TextView score = view.findViewById(R.id.score);
        score.setText("Score:"+((ScaffoldActivity)getActivity()).score);
    }

    @Override
    public void onClick(View v)
    {
        switch ((v.getId())){
            case R.id.btn_menu:
                ((ScaffoldActivity)getActivity()).mainMenu();
                break;
            case R.id.btn_restart:
                ((ScaffoldActivity)getActivity()).startGame(0); //0 no es ninguna nave asi que el startGame lo ignora, dejando la nave anterior
                break;
            case R.id.btn_reselect:
                ((ScaffoldActivity)getActivity()).shipSelection();
                break;
        }
    }
}

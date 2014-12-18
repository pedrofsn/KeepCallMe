package keep.call.me;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

public class Dialog extends DialogFragment implements OnClickListener {

    private Button listaDeContatos;
    private Button manualmente;

    public Dialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog, container);

        listaDeContatos = (Button) view.findViewById(R.id.listaDeContatos);
        manualmente = (Button) view.findViewById(R.id.manualmente);

        listaDeContatos.setOnClickListener(this);
        manualmente.setOnClickListener(this);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // Show soft keyboard automatically

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.listaDeContatos:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1);
                break;
            case R.id.manualmente:
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "aaaaaaaaa"));
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

}

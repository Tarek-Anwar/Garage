package com.HomeGarage.garage.ui.navfragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.dialog.DialogPurchase;
import com.braintreepayments.cardform.view.CardForm;


public class PayFragment extends Fragment {

    CardForm cardForm;
    Button btn_pay;
    AlertDialog.Builder builder;
    Toast toast;
    public PayFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pay, container, false);
        initUI(root);

        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.castom_toast_layout,root.findViewById(R.id.custom_toast_thank_you));
        toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(view);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .actionLabel("Purchase")
                .setup(getActivity());

        cardForm.getCardEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        btn_pay.setOnClickListener(v -> {
            if(cardForm.isValid()){
                builder= new AlertDialog.Builder(getContext());
                DialogPurchase dialogPurchase = new DialogPurchase();
                dialogPurchase.show(getParentFragmentManager(),"Purchase");
            }else {
                Toast.makeText(getContext(), "Please complete the form", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    void initUI(View v){
        cardForm  = v.findViewById(R.id.card_form);
        btn_pay = v.findViewById(R.id.btn_pay_card);
    }
}
package com.example.biometria3a;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InfoExtraFragment extends Fragment {

    // O3 部分
    private TextView ozonoText, recomendacionesOzonoText, precaucionOzonoText;
    private TextView ozonoTitle, recomendacionesOzonoTitle, precaucionOzonoTitle;

    // NO2 部分
    private TextView no2Text, recomendacionesNo2Text, precaucionNo2Text;
    private TextView no2Title, recomendacionesNo2Title, precaucionNo2Title;

    // SO3 部分
    private TextView so3Text, recomendacionesSo3Text, precaucionSo3Text;
    private TextView so3Title, recomendacionesSo3Title, precaucionSo3Title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_extra, container, false);

        // 设置 "documento oficial" 为可点击的链接
        TextView enlaceDocumento = view.findViewById(R.id.enlace_documento_oficial);
        String text = "Para más información sobre la normativa y recomendaciones sobre la calidad del aire, consulta el siguiente documento oficial.";
        SpannableString spannableString = new SpannableString(text);

        // 设置 "documento oficial" 为可点击文本
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = "https://www.boe.es/buscar/doc.php?id=DOUE-L-2008-80787";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        };

        int startIndex = text.indexOf("documento oficial");
        int endIndex = startIndex + "documento oficial".length();
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        enlaceDocumento.setText(spannableString);
        enlaceDocumento.setMovementMethod(LinkMovementMethod.getInstance());

        // 初始化控件
        initViews(view);

        // 设置点击事件
        setupToggleVisibilityWithArrow(ozonoTitle, ozonoText);
        setupToggleVisibilityWithArrow(recomendacionesOzonoTitle, recomendacionesOzonoText);
        setupToggleVisibilityWithArrow(precaucionOzonoTitle, precaucionOzonoText);

        setupToggleVisibilityWithArrow(no2Title, no2Text);
        setupToggleVisibilityWithArrow(recomendacionesNo2Title, recomendacionesNo2Text);
        setupToggleVisibilityWithArrow(precaucionNo2Title, precaucionNo2Text);

        setupToggleVisibilityWithArrow(so3Title, so3Text);
        setupToggleVisibilityWithArrow(recomendacionesSo3Title, recomendacionesSo3Text);
        setupToggleVisibilityWithArrow(precaucionSo3Title, precaucionSo3Text);

        return view;
    }

    private void initViews(View view) {
        // 初始化 O3 部分控件
        ozonoText = view.findViewById(R.id.ozono_text);
        recomendacionesOzonoText = view.findViewById(R.id.recomendaciones_ozono_text);
        precaucionOzonoText = view.findViewById(R.id.precaucion_ozono_text);
        ozonoTitle = view.findViewById(R.id.ozono_title);
        recomendacionesOzonoTitle = view.findViewById(R.id.recomendaciones_ozono_title);
        precaucionOzonoTitle = view.findViewById(R.id.precaucion_ozono_title);

        // 初始化 NO2 部分控件
        no2Text = view.findViewById(R.id.no2_text);
        recomendacionesNo2Text = view.findViewById(R.id.recomendaciones_no2_text);
        precaucionNo2Text = view.findViewById(R.id.precaucion_no2_text);
        no2Title = view.findViewById(R.id.no2_title);
        recomendacionesNo2Title = view.findViewById(R.id.recomendaciones_no2_title);
        precaucionNo2Title = view.findViewById(R.id.precaucion_no2_title);

        // 初始化 SO3 部分控件
        so3Text = view.findViewById(R.id.so3_text);
        recomendacionesSo3Text = view.findViewById(R.id.recomendaciones_so3_text);
        precaucionSo3Text = view.findViewById(R.id.precaucion_so3_text);
        so3Title = view.findViewById(R.id.so3_title);
        recomendacionesSo3Title = view.findViewById(R.id.recomendaciones_so3_title);
        precaucionSo3Title = view.findViewById(R.id.precaucion_so3_title);
    }

    private void setupToggleVisibilityWithArrow(final TextView titleView, final TextView contentView) {
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contentView.getVisibility() == View.GONE) {
                    contentView.setVisibility(View.VISIBLE);
                    titleView.setText(titleView.getText().toString().replace("▼", "▲"));
                } else {
                    contentView.setVisibility(View.GONE);
                    titleView.setText(titleView.getText().toString().replace("▲", "▼"));
                }
            }
        });
    }
}

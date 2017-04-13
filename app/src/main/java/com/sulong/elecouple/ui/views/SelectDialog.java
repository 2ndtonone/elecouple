package com.sulong.elecouple.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sulong.elecouple.R;

public class SelectDialog extends Dialog {

    public SelectDialog(Context context) {
        super(context);
    }

    public SelectDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String text1;
        private String text2;
        private String negativeButtonText;
        private View contentView;
        private OnClickListener text1Listener;
        private OnClickListener text2Listener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setText1(String text,
                                OnClickListener listener) {
            this.text1 = text;
            this.text1Listener = listener;
            return this;
        }

        public Builder setText2(String text,
                                OnClickListener listener) {
            this.text2 = text;
            this.text2Listener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public SelectDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final SelectDialog dialog = new SelectDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_normal_layout, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            ((TextView) layout.findViewById(R.id.title)).setText(title);

            if (text1 != null) {
                ((TextView) layout.findViewById(R.id.message))
                        .setText(text1);
                if (text1Listener != null) {
                    ((TextView) layout.findViewById(R.id.message))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    text1Listener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.message).setVisibility(
                        View.GONE);
            }

            if (text2 != null) {
                ((TextView) layout.findViewById(R.id.message1))
                        .setText(text2);
                if (text2Listener != null) {
                    ((TextView) layout.findViewById(R.id.message1))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    text2Listener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.message1).setVisibility(
                        View.GONE);
            }

            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE  
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            // set the content message  
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (contentView != null) {
                // if no message set  
                // add the contentView to the dialog body  
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}  
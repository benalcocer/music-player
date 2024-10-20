package com.example.views;

public class FormView extends GridView {

    public FormView() {
        super();
        getStyleClass().add("form-view");
    }

    public void addInputViews(InputView... inputViews) {
        for (InputView inputView : inputViews) {
            addInputView(inputView);
        }
    }

    public void addInputView(InputView inputView) {
        addRow(inputView.getPrefixLabel(), inputView);
    }
}

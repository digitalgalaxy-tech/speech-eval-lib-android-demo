# Android Sample App

## Setup
Put your credentials into App.java:
```
String email = null;
String uuid = null;
```

Build and run the app.

## API
Usage sample:
```
Callback callback = new Callback() {
    @Override
    public void onResult(int stage, String result) {
        super.onResult(stage, result);
    }
};
VoiceComparisonSDK.compare(callback, context, Format.JSON, wav1, wav2, text);
```
A first call(stage=1) of callback will return total score, a second call(stage=2) - detailed information.

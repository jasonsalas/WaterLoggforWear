# WaterLogg for Wear - update your Fitbit profile on your wrist...via voice or touch!
## A utility integrating Fitbit with Android Wear

![WaterLogg for Wear lets you update your Fitbit profile with Android Wear using voice commands or a list of tappable values](https://dl.dropboxusercontent.com/u/12019700/glass-dev/tester-images/waterloggforwear-screencap.png) ![WaterLogg for Wear notifies you when you've successfully updated your Fitbit profile](https://dl.dropboxusercontent.com/u/12019700/glass-dev/tester-images/waterloggforwear-confirm.png)

### Genesis
Fitbit is one of the most fun wearable computing devices around, and it's frequently used in hackathons as the base for neat ideas. This is an expansion of another wearble technology project I maintain, [WaterLogg for Google Glass](https://github.com/jasonsalas/WaterLoggforGlass).

- [My slide decks describing the project](https://docs.google.com/presentation/d/1Gy1Rf0oeZL4WClsBgvcPEN4Sm2qZ0KA1VfJlU8iK3Z4/present?slide=id.g57389a31e_06)

Still, the one use case that wasn't working for me was having to repeatedly log intake in Fitbit's mobile and web apps gets laborious and often takes the user away from doing more important things. This is precisely the problem that Android Wear solves - decoupling you from technology and not taking you out of the moment! This simple service is an [Android Studio](http://developer.android.com/tools/studio/index.html) project using the [Message API](https://developer.android.com/reference/com/google/android/gms/wearable/MessageApi.html) lets you use handsfree voice dictation to post updates everytime you hydrate, which updates your Fitbit profile.

### Usage
After you've granted access to your Google and Fitbit profiles, use the _"OK Google...Start WaterLogg...33"_ command on your connected Android Wwar device (like a smartwatch), and then speak the volume of water you've just consumed. It'll be reflected on your [Fitbit dashboard](https://www.fitbit.com/).
		
### Libraries/APIs
This app uses the following libraries and APIs:

- [Android SDK Message API](https://developer.android.com/training/wearables/data-layer/messages.html) to send messages from the wearable device to th connected handheld device, which then handles the update with the cloud
- [Android's Speech Recognizer](http://developer.android.com/reference/android/speech/RecognizerIntent.html) which transcribes spoken free-form input into text...it handles numerics incredibly well!
- [WearableListenerService](https://developer.android.com/reference/com/google/android/gms/wearable/WearableListenerService.html) an extended class allows for messages to be recieved and processed
- [WearableListView](https://developer.android.com/training/wearables/ui/lists.html) extends the ability to scroll through pre-seeded values in a list and select them when using voice isn't possible (like in line at the bank, in court, in a crowd, at the library, etc.)
- [GoogleApiClient](https://developer.android.com/reference/com/google/android/gms/common/api/GoogleApiClient.html) for integration with Google Play Services, needed for the cross-device communication
- [Temboo's Fitbit library](https://www.temboo.com/library/Library/Fitbit/) to manage the [OAuth 1.0 flow and credentials](https://wiki.fitbit.com/display/API/OAuth+Authentication+in+the+Fitbit+API) and make the necessary HTTP requests to communicate with the [Fitbit API](https://www.fitbit.com/dev/dev). Once you generate the app keys and credentials on Temboo's site and grant access in a desktop browser, they can be copied over into your project in a single step without needing to use an embedded authorization URL in a WebView. However, you can still rebuild the OAuth flow by using [Temboo's libs](https://www.temboo.com/library/Library/Fitbit/OAuth/).

_Disclaimer: This was written as a personal utility and for public demonstration purposes, but I don't work for Google, Fitbit, or Temboo. I'm just a total fanboy._

---

You can find out more Glassware tips in my book, [Designing and Developing for Google Glass](http://www.amazon.com/Designing-Developing-Google-Glass-Differently/dp/1491946458), by O'Reilly & Associates. **Thanks for your support! :)**

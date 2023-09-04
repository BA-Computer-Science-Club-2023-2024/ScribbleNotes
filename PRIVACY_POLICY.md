# Privacy Policy

This privacy policy goes into detail about how your data is used and collected. All of the code for this app (ScribbleNotes) is licensed under the MIT license in this GitHub repository.

This app and privacy policy are developed by VeryRandomCreator.

When this privacy policy mentions "I", it refers to the developer or the writer of the privacy policy and developer of the app, VeryRandomCreator. When this privacy policy mentions "you" or "your", it refers to the user or the belonging(s) of the user, respectively.

## Personal Information

I have not developed this app with any intention or knowledge that personal information about the user is sent or used. Personal information is information such as your name, phone number, email, address, or anything identifiable.

## Device Information

No information unique to your device (ex: ids) is saved or sent.

## Permissions

### `POST_NOTIFICATIONS`

The `android.permission.POST_NOTIFICATIONS` permission is requested in the 'AndroidManifest.xml' file:

https://github.com/BA-Computer-Science-Club-2023-2024/ScribbleNotes/blob/e1670e164d69fb8bddc544be1533dc7436dce6c5/app/src/main/AndroidManifest.xml#L8

This permission allows the app to start the notifications needed to create the foreground services to run the file transfering mechanism.

### `FOREGROUND_SERVICE`

The `android.permission.FOREGROUND_SERVICE` permission is requested in the 'AndroidManifest.xml' file:

https://github.com/BA-Computer-Science-Club-2023-2024/ScribbleNotes/blob/e1670e164d69fb8bddc544be1533dc7436dce6c5/app/src/main/AndroidManifest.xml#L7C1-L7C1

This permission allows the app to create a foreground service for the client and server parts of the app

### `VIBRATE`

The `android.permission.VIBRATE` permission is requested in the 'AndroidManifest.xml' file:

https://github.com/BA-Computer-Science-Club-2023-2024/ScribbleNotes/blob/e1670e164d69fb8bddc544be1533dc7436dce6c5/app/src/main/AndroidManifest.xml#L6

This permission allows the app to vibrate the device.

### `RECEIVE_BOOT_COMPLETE`

The `android.permission.RECEIVE_BOOT_COMPLETE` permission is requested in the 'AndroidManifest.xml' file:

https://github.com/BA-Computer-Science-Club-2023-2024/ScribbleNotes/blob/e1670e164d69fb8bddc544be1533dc7436dce6c5/app/src/main/AndroidManifest.xml#L5

This permission allows the create note notification to launch after boot.

## Attributions

This project contains modified code based on work created and shared by the Android Open Source Project and used according to terms described in the [Creative Commons 2.5 Attribution License](https://creativecommons.org/licenses/by/2.5/legalcode).

The following are the sources of the modified code from the [Android Developer Website](https://developer.android.com/):
 * [Create a Notification](https://developer.android.com/develop/ui/views/notifications/navigation)
 * [Broadcast Receivers](https://developer.android.com/guide/components/broadcasts)
 * [Fragments](https://developer.android.com/guide/fragments/create)
 * [ListViews](https://developer.android.com/reference/android/widget/ListView)
 * [Foreground Services](https://developer.android.com/guide/components/foreground-services)

### Apache License Version 2.0

This project contains code and modified code of the vector images of the Material Design icons by Google. These icons are under the [Apache License Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).

All of the vector assets used in the project are subject to this license.

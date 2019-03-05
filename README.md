# Magic Wormhole on Android

This is a simple proof of concept Android app of the Magic Wormhole
protocol on Android and uses the Rust port of the protocol to provide
the underlying functionality.

# Building

- Install Android SDK and NDK.
- Install Rust compiler (I use `rustup` utility to manage the rust compiler installation).
- Install the Rust cross compilers for Android (including x86 ones for testing on the emulator).

```
rustup target add arm-linux-androideabi
rustup target add aarch64-linux-android
rustup target add x86-linux-android
```

- setup a few environment variables.

```
export ANDROID_NDK_HOME=/path/to/your/android/ndk
mkdir -p /path/to/rust-ndk
export RUST_NDK=/path/to/rust-ndk
```

- create NDK toolchains for Android.

```
${ANDROID_NDK_HOME}/build/tools/make_standalone_toolchain.py --api 28 --arch arm64 --install-dir ${RUST_NDK}/arm64
${ANDROID_NDK_HOME}/build/tools/make_standalone_toolchain.py --api 28 --arch arm --install-dir ${RUST_NDK}/arm
${ANDROID_NDK_HOME}/build/tools/make_standalone_toolchain.py --api 28 --arch x86 --install-dir ${RUST_NDK}/x86
```
- create a file `~/.cargo/config` containing the following (modify the paths according to your installation)

```
[target.aarch64-linux-android]
ar = "/path/to/rust-ndk/arm64/bin/aarch64-linux-android-ar"
linker = "/path/to/rust-ndk/arm64/bin/aarch64-linux-android-clang"

[target.armv7-linux-androideabi]
ar = "/path/to/rust-ndk/arm/bin/arm-linux-androideabi-ar"
linker = "/path/to/rust-ndk/arm/bin/arm-linux-androideabi-clang"

[target.i686-linux-android]
ar = "/path/to/rust-ndk/x86/bin/i686-linux-android-ar"
linker = "/path/to/rust-ndk/x86/bin/i686-linux-android-clang"
```

- Clone this repo and the submodules.

```
git clone https://github.com/LeastAuthority/magic-wormhole.git WormholeApp
cd WormholeApp
git submodule update --init --recursive
```

- Invoke the build script to build the entire app.

```
./build-all.sh
```

# Testing

I used `android studio` to create an emulator and run the app (`.apk` file) on the target.

However, be aware that `android studio` is bloatware and with `adb` and such tools, it is
possible to install the app on a real phone or an emulator.

# Warning

The App is still not fully functional. So, expect crashes.



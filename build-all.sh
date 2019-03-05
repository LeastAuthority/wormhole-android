#!/bin/sh

export ANDROID_NDK_HOME=~/work/leastauthority/android/ndk/android-ndk-r17b/
export PATH=~/work/leastauthority/android/ndk-rust/arm/bin:~/work/leastauthority/android/ndk-rust/arm64/bin:~/work/leastauthority/android/ndk-rust/x86/bin:$PATH

# build rust components

echo "Building rust magic-wormhole library"

cd magic-wormhole.rs

env CC_aarch64-linux-androideabi=aarch64-linux-androideabi-gcc PKG_CONFIG_ALLOW_CROSS=1 RUSTFLAGS='-L /home/ram/work/leastauthority/nlnet/brian/libsodium/libsodium-android-armv8-a/lib/'  cargo build --target aarch64-linux-android --release

env CC_armv7-linux-androideabi=arm-linux-androideabi-gcc PKG_CONFIG_ALLOW_CROSS=1 RUSTFLAGS='-L /home/ram/work/leastauthority/nlnet/brian/libsodium/libsodium-android-armv7-a/lib/'  cargo build --target armv7-linux-androideabi --release

env CC_i686-linux-androideabi=i686-linux-android-gcc PKG_CONFIG_ALLOW_CROSS=1 RUSTFLAGS='-L /home/ram/work/leastauthority/nlnet/brian/libsodium/libsodium-android-i686/lib/'  cargo build --target i686-linux-android --release

cd ..

# copy resultant shared libraries into jniLibs

echo "Copying the library into the app jniLibs directory"

# delete existing copies of .so files
rm app/src/main/jniLibs/arm64/libmagic_wormhole_io_blocking.so
rm app/src/main/jniLibs/x86/libmagic_wormhole_io_blocking.so
rm app/src/main/jniLibs/armeabi/libmagic_wormhole_io_blocking.so

# copy newly built versions
cp magic-wormhole.rs/target/aarch64-linux-android/release/libmagic_wormhole_io_blocking.so app/src/main/jniLibs/arm64/
cp magic-wormhole.rs/target/i686-linux-android/release/libmagic_wormhole_io_blocking.so app/src/main/jniLibs/x86/
cp magic-wormhole.rs/target/armv7-linux-androideabi/release/libmagic_wormhole_io_blocking.so app/src/main/jniLibs/armeabi/

# now build the java app

echo "Building the Android Java App"
./gradlew assembleDebug

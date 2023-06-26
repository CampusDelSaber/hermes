apk_path='app/build/outputs/apk/release/*.apk'
id=$1

echo $(curl -s -T $apk_path "https://transfer.sh/hermes-$id.apk")

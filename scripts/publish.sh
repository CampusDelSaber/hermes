apk_path='app/build/outputs/apk/release/*.apk'
webhook_url='https://universidadsalesian.webhook.office.com/webhookb2/453cc857-0fbf-4e9d-9ffe-0b4987995cfe@06048c87-405a-4492-9946-b169a82c64ac/IncomingWebhook/45d280f5824d46fb9507d20761a39d51/4829b305-b733-4ce6-9f0f-695a10f74447'

url_id=$1

# Upload apk
download_link=$(curl -s -T $apk_path "https://transfer.sh/hermes-$url_id.apk")

# Notify to subscribers
curl -s -X POST -H 'Content-Type: application/json' -d @- "$webhook_url" <<EOF
{
  "title": "CI/CD Notification",
  "text": "Build completed successfully",
  "themeColor": "good",
  "potentialAction": [
    {
      "@type": "OpenUri",
      "name": "APK Download",
      "targets": [
        {
          "os": "default",
          "uri": "$download_link"
        }
      ]
    }
  ]
}
EOF

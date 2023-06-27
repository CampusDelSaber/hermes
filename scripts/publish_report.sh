webhook_url='https://universidadsalesian.webhook.office.com/webhookb2/453cc857-0fbf-4e9d-9ffe-0b4987995cfe@06048c87-405a-4492-9946-b169a82c64ac/IncomingWebhook/45d280f5824d46fb9507d20761a39d51/4829b305-b733-4ce6-9f0f-695a10f74447'

job_name=$1
report_url=$2


# Notify to subscribers
curl -s -X POST -H 'Content-Type: application/json' -d @- "$webhook_url" <<EOF
{
  "title": "CI/CD Notification",
  "text": "Job $job_name has failed!!",
  "themeColor": "attention",
  "potentialAction": [
    {
      "@type": "OpenUri",
      "name": "Open report",
      "targets": [
        {
          "os": "default",
          "uri": "$report_url"
        }
      ]
    }
  ]
}
EOF

exit 0

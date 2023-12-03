#!/usr/bin/expect

spawn git push -u origin main
expect "Username for 'https://github.com':"
send "$1\r"

expect "Password for 'https://$1@github.com':"
send "$2\r"

interact

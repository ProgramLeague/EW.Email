# EW.Email v1.0.0 ![](https://www.gnu.org/graphics/gplv3-127x51.png)
Use email to wake your PC, and so on.

## Screenshot

Sent email with subject `alive & ip & wake Ray Eldath's Desktop`:

![System Log](https://res.cloudinary.com/ray-eldath/image/upload/v1506840970/github/ew/snipaste20171001_143721.png)

And you will receive:

![Received](https://res.cloudinary.com/ray-eldath/image/upload/v1506840971/github/ew/snipaste20171001_143936.png)

## Features
 - **Multi-commends email (email whose subject is multiple commends separate by `&`, like `alive & ip & wake Ray Eldath's Desktop`) supported.**
 - **Useful commend handlers contained**: 
   - `wake <TARGET NAME>`: sent magic package to target in order to wake target device.
   - `ip`: returns public IP address.
   - `alive|heartbeat`: test if server still alive.
 - **Support mainstream mail protocol (`POP3`, `IMAP`, `SMTP`).**
 - **Easy to expand.**

## Using

## Basic using

1. Decompression RELEASE file which downloaded [here](github.com/ProgramLeague/EmailEverything/releases/latest).
2. Create file `config.json` under directory `EW.Email-VERSION/bin`.
3. Config `config.json` which describe in next part.
4. Execute file `EW.Email` (Linux) or `EW.Email.bat` (Windows).
5. Star and Follow this project :-)
## Start when system started

**Following instructions are for Linux system.** *(Tested on Debian 8)* 

**For Windows, just add `EW.Email.bat` into your list of startup items.**

1. Download [ew](https://github.com/ProgramLeague/EW.Email/blob/master/ew) file and move it into `/etc/init.d`. (remember in `sudo`! )
2. Use `chkconfig` to set it start automatically.

## Configuration

This part explains config file `config.json`.

`config.json`:
```json
{
  "update_frequency": 60,             // Frequency of connect email server and handle email.
  "daemon": true,                     // Use daemon mode. If on, log will outputs into temp file, not stdout.
  "zone_id": "Asia/Shanghai",         // Your zone id, used for zoned time. All available values see `https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html#SHORT_IDS`.
  "permitted_addresses": [            // Emails from these addresses will be handled.
    "ray.eldath@gmail.com"

  ],
  "email": {
    "name": "Ray Eldath",
    "address": "test@live.cn",          // Your email address, also used for login email server.
    "password": "TestForTest@!%!",      // Password of your email.
    "sender": {                         // Config sender server.
      "protocol": "SMTP",               // Only support SMTP protocol yet.
      "host": "smtp-mail.outlook.com",  // Server host.
      "port": 587,                      // Server port.
      "transport_strategy": "SMTP_TLS"  // Transport strategy, available values containes `SMTP_PLAIN` `SMTP_SSL` and `SMTP_TLS`. Default `SMTP_PLAIN`.
    },
    "receiver": {
      "protocol": "IMAP",               // Support IMAP and POP3 protocols.
      "host": "imap-mail.outlook.com",
      "port": 993,
      "ssl": true                       // Use SSL or not. Default `false`.
    }
  },
  "handler": {                          // Config for commend handler
    "Wake": {                           // Config for handler:Wake.
      "target": [
        {
          "name": "Ray Eldath's Laptop",   // Name of this target. You should wake this device with email with subject `wake Ray Eldath's Laptop`.
          "mac": "4C:CC:6A:30:C2:5A",      // MAC of the device.
          "ip": "192.168.0.8"              // IP of the device.
        }
      ]
    }
  },
  "debug": false                           // Only for debug purpose.
}
```

## Expend
This part describes how to add custom commend handler into EmailEverything.
1. Created `class` under package `ray.eldath.ew.handler`
2. Make this `class` `implements Handler`
3. Let method `titleRegex` returns regex for email subject
4. Fill main processing in method `handle`
5. Register your handler in array `MainHandler/allHandler`
6. Star and Follow this project :-)
## **iDownload**

#### **Compile**
`./gradlew bootjar`

#### **Run**
Minimum usage for HTTP

`java -jar build/libs/iDownload-0.0.1.jar --url <url> --path <path>
`

Minimum usage for FTP

`java -jar build/libs/iDownload-0.0.1.jar --url <url> --path <path> --user <user name> --pass <password>
`

Manually decide number of connections to download via http (_servers supporting partial request only_)

If not given number of connection will be decided from file size.

`java -jar build/libs/iDownload-0.0.1.jar --url <url> --path <path> --con <numnber of connections>
`

Help menu

`java -jar build/libs/iDownload-0.0.1.jar --help
`

#### **Demo**

demo video is inside documentation folder.


#### **Run Unit Tests**

`./gradlew test`

Test report will be available in 

`/build/reports/tests/test/packages/com.example.idownload.html`



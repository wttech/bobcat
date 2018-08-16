# Introduction
This document describes Bobcat release procedure for Bobcat contributors. To become a contributor, please check our [contribution guidelines](https://github.com/Cognifide/bobcat/blob/master/CONTRIBUTING.md).

Original guide by @Shaihuludus.

# Prerequisites

## Sonatype's OSS Nexus deployment rights

You are required to have an authorized account to release Bobcat artifacts to Sonatype's Nexus. To obtain such rights, create an account at https://issues.sonatype.org and ask for permissions to group com.cognifide. If you are Cognifide employee, use your company email address - otherwise, you need to contact us first.
>Make sure you are using Community Support - Open Source Project Repository Hosting for the issue. Example JIRA: [link](https://issues.sonatype.org/browse/OSSRH-26531)

## GPG Signatures
To release to Maven Central files need to be signed. Every user who is performing release has to have his own key. Public key should be published.

[How to generate keys](http://central.sonatype.org/pages/working-with-pgp-signatures.html) - this is the general document about key generation, however few tweaks can be added:

1. (For Windows) https://www.gpg4win.org/download.html use this to get gpg installation. It also comes with a nice GUI (Kleopatra) which makes key management easy
2. `gpg --gen-key` or use Kleopatra for OpenPGP certificate (RSA and 2048 size both methods)
3. *Before* key publishing it is a good idea to generate a revocation certificate. This can be done only from command line with `gpg --output revoke.asc --gen-revoke key-id`. Id can be retrieved using `gpg2 --list-keys` - the first line `pub <size>/<key-id> <other things>`. The `revoke.asc` is the name of the file where we want to keep the revocation certiicate.
4. Then we can publish our public key. In Kleopatra simply use default options. When doing it via command line, run: `gpg2 --keyserver hkp://pool.sks-keyservers.net --send-keys key-id`
5. (Only for Windows) Create new environment variable `GNUPGHOME` and set it similar to `C:\Users\<username>\AppData\Roaming\gnupg`.

## Maven Settings
After we have the keys generated, we have to point to them in our Maven settings in `settings.xml`:

```xml
<servers>
(...)
   <server>
      <id>${gpg.keyname}</id>
      <passphrase>password for key</passphrase>
   </server>
   <server>
      <id>sonatype-nexus-staging</id>
      <username>sonatype username</username>
      <password>
            sonatype password
      </password>
    </server>
</servers>
```

Ideally, passowrds should be encrypted: [link](https://maven.apache.org/guides/mini/guide-encryption.html)

In addition, create a profile for executing releases:

```
<profile>
    <id>sonatype-oss-release</id>
        <activation>
                <activeByDefault>false</activeByDefault>
        </activation>
        <properties>
                <privateKey>C:\Users\<username>\.ssh\id_dsa.pub</privateKey>
                <scm.username>git</scm.username>
                <passphrase>private key password if it exists</passphrase>
                <gpg.keyname>gpg KEY_ID</gpg.keyname>
                <gpg.executable>gpg2</gpg.executable>
        </properties>
</profile>
```

(gpg.executable should be set on Windows only)
`KEY_ID` here is the id of our gpg key.

We are ready for release!

# Release

## 1. Performing the Maven release
>On Windows ssh and git must be available from console (git bash is a perfect choice). Also, if there is any Mavne profile default by active, which has different properties than `sonatype-oss-release`, it must be disabled in both commands for release (!profile or \!profile if escape is required).

1. To release Bobcat, simply run: `mvn release:prepare -Psonatype-oss-release`.
You will be prompted for the version number for each module of the project, a tag name, and the next SNAPSHOT versions. You will also need to type the passphrase for the key, which you generated during Generating a PGP Signatures step and optional for the ssh key used to access GitHub. When this command finishes successfully, the version section of each module's pom.xml file will be updated.
2. If everything is correct, run `mvn release:perform -Psonatype-oss-release` to finalize the release procedure. Again, you will be prompted to provide passwords.

## 2. Releasing in Nexus
If staging release with Maven ended successfully, then:

1. Go to Nexus UI: https://oss.sonatype.org/ and log in using your [Sonatype JIRA](https://issues.sonatype.org/) credentials.
2. Go to [Staging Repositories](https://oss.sonatype.org/index.html#stagingRepositories) page and select the Bobcat staging repository (it should be at the end of the list, it can take a while to appear on the list. If it does not appear in the end, that means the Maven release process failed).
3. Click the **Close** button, type closing description. If the staging repository is closed successfully, you will get a notification email and confirmation via the UI.
4. Select the Bobcat staging repository again, click the **Release** button, providing some description.

# Post-release steps
  
## Gradle template

1. Bump Bobcat's version in the https://github.com/Cognifide/bobcat-gradle-template/


# References

* https://maven.apache.org/guides/mini/guide-central-repository-upload.html
* http://central.sonatype.org/pages/requirements.html

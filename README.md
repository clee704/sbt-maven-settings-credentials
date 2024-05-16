# How to use

Requirements: SBT 1.9.9 or higher.

Add the following line to `project/plugins.sbt`:

```scala
addSbtPlugin("dev.chungmin" % "sbt-maven-settings-credentials" % "0.0.1")
```

The plugin will try to find credentials from `~/.m2/settings.xml` for the resolvers defined in `build.sbt`.

# Example

```scala
// build.sbt
resolvers += "InternalMaven" at "https://myorg.pkgs.visualstudio.com/myproject/_packaging/InternalMaven/maven/v1"
```

```xml
<!-- ~/.m2/settings.xml -->
<?xml version='1.0' encoding='utf-8'?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0                               https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>InternalMaven</id>
      <username>myorg</username>
      <password>mypass</password>
    </server>
  </servers>
</settings>
```

If there is `<server>` whose `<id>` matches the resolver name, username and password will be read from it. The plugin will append `Credentials("<realm>", "myorg.pkgs.visualstudio.com", "myorg", "mypass")` to `credentials`. The realm value is retrieved by making an HTTP request to the resolver's root URL and looking at the `WWW-Authenticate` header in the response.

You can run `sbt 'show credentials'` to check if the credentials are populated correctly.

# Known limitations

- It doesn't support alternative paths for `settings.xml`. Notably, `M2_HOME` is not supported.
- The realm value might be parsed incorrectly if it contains escape sequences.
- It requires SBT 1.9.9 or higher.

None of these are too difficult to fix, but the current code just works for me so I didn't bother to invest more time. Contributions are welcome.

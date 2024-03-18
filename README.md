# Java Spintax Parser

## Simple, stateful spintax parsing for Java.
Up until now I haven't seen an implementation for Spintax in Java.
This provides full spintax functionality, with support for nesting and duplicate avoidance.
It's based on the algorithm described in my article here: 
https://www.toughdata.net/blog/post/spintax-parser-python

## Usage

Allow duplicates in spintax generation
```java
String content = "{hello|world}";

// Initialize with content and allowDuplicates
ContentSpinner spinner = new ContentSpinner(content, true);

spinner.generate();
```

Do not allow duplicates in spintax processing (experimental).
`TODO`: Calculate number of possible spintax combinations to avoid `StackOverflowError`.
```java
String content = "{hello|world}";

// Initialize with content and allowDuplicates
ContentSpinner spinner = new ContentSpinner(content, true);

spinner.generate();

```

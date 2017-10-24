# Generate doc

We use a Maven tool to generate our documentation from JAX-RS annotation.

This tool will generate a `swagger.json` doc in the `target/jaxrs-analyzer` directory.

## HTML Doc

**Pre-requisite:** [Spectacle](https://github.com/sourcey/spectacle/) (`npm install -g spectacle-docs`)

To re-generate HTML doc, simply run:
```
spectacle -l logo.png ../../../target/jaxrs-analyzer/swagger.json
```
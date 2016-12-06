# digi-signer-java-client

## Overview

The digi-signer-java-client is a java library for the Digi-Signer application.

## Installation

To build the jar, get the digi-signer-java-client project and run maven with *mvn* *install* command.

## Usage

The main class is *DigiSignerClient*.

```
new DigiSignerClient(API_KEY);
```

The client can make following commands:

| Command name  | Parameters           | Returns|Description  |
| ------------- |:-------------:| -----:| -----:|
| uploadDocument      | Document | Document (created instace with ID) | Uploads document to the Digi-Signer application |
| getDocumentById      | String documentId, String fileName      |   Document | Gets document with content |
| callUploadDocument | Document      |    ID of created document as String | Upload document and returns ID of document |
| getDocumentFields | String documentId      |   DocumentFields |  Returns document fields for a document if any exist |
| addContentToDocument | String documentId  , List<Signature> signatures    |  void | Adds content to the document after given document ID. |
| sendSignatureRequest | SignatureRequest      |   SignatureRequest | Sends the signature request to the Digi-Signer application. |
| getSignatureRequest | String signatureRequestId      |   SignatureRequest |  Gets signature request to check information about signature such as signature is completed and IDs of signature request and documents. |

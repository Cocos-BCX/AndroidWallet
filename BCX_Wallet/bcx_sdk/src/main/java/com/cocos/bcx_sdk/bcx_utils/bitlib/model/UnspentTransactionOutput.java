package com.cocos.bcx_sdk.bcx_utils.bitlib.model;

import com.cocos.bcx_sdk.bcx_utils.bitlib.util.ByteReader;
import com.cocos.bcx_sdk.bcx_utils.bitlib.util.ByteWriter;

import java.io.Serializable;

public class UnspentTransactionOutput implements Serializable {
   private static final long serialVersionUID = 1L;
   
   public OutPoint outPoint;
   public int height; // -1 means unconfirmed
   public long value;
   public ScriptOutput script;

   public UnspentTransactionOutput(ByteReader reader) throws ByteReader.InsufficientBytesException, Script.ScriptParsingException {
      outPoint = new OutPoint(reader);
      height = reader.getIntLE();
      value = reader.getLongLE();
      int scriptSize = (int) reader.getCompactInt();
      byte[] scriptBytes = reader.getBytes(scriptSize);
      script = ScriptOutput.fromScriptBytes(scriptBytes);
   }

   public UnspentTransactionOutput(OutPoint outPoint, int height, long value, ScriptOutput script) {
      this.outPoint = outPoint;
      this.height = height;
      this.value = value;
      this.script = script;
   }

   public byte[] toBytes() {
      ByteWriter writer = new ByteWriter(1024);
      toByteWriter(writer);
      return writer.toBytes();
   }

   public void toByteWriter(ByteWriter writer) {
      outPoint.toByteWriter(writer);
      writer.putIntLE(height);
      writer.putLongLE(value);
      byte[] scriptBytes = script.getScriptBytes();
      writer.putCompactInt(scriptBytes.length);
      writer.putBytes(scriptBytes);
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("outPoint:").append(outPoint).append(" height:").append(height).append(" value: ").append(value)
            .append(" script: ").append(script.dump());
      return sb.toString();
   }

   @Override
   public int hashCode() {
      return outPoint.hash.hashCode() + outPoint.index;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }
      if (!(obj instanceof UnspentTransactionOutput)) {
         return false;
      }
      UnspentTransactionOutput other = (UnspentTransactionOutput) obj;
      return outPoint.equals(other.outPoint);
   }

}

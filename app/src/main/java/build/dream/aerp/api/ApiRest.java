package build.dream.aerp.api;

import android.util.Base64;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import build.dream.aerp.constants.Constants;
import build.dream.aerp.exceptions.Error;
import build.dream.aerp.utils.ApplicationHandler;
import build.dream.aerp.utils.JacksonUtils;
import build.dream.aerp.utils.RSAUtils;
import build.dream.aerp.utils.SignatureUtils;
import build.dream.aerp.utils.ZipUtils;

/**
 * Created by liuyandong on 2017/3/20.
 */
public class ApiRest implements Serializable, Cloneable {
    private boolean successful;
    private Object data;
    private String className;
    private String message;
    private Error error;
    private String id;
    private String timestamp;
    private String signature;
    private boolean zipped;
    private boolean encrypted;

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestamp(Date date) {
        this.timestamp = new SimpleDateFormat(Constants.DEFAULT_DATE_PATTERN).format(date);
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean isZipped() {
        return zipped;
    }

    public void setZipped(boolean zipped) {
        this.zipped = zipped;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public static ApiRest fromJson(String jsonString) {
        return fromJson(jsonString, Constants.DEFAULT_DATE_PATTERN);
    }

    public static ApiRest fromJson(String jsonString, String datePattern) {
        ApiRest apiRest = JacksonUtils.readValue(jsonString, ApiRest.class, datePattern);
        if (apiRest.isEncrypted()) {
            apiRest.decryptData(ApplicationHandler.privateKeyString, datePattern);
        }

        if (apiRest.isZipped()) {
            apiRest.unzipData(datePattern);
        }
        if (StringUtils.isNotBlank(apiRest.className)) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(apiRest.className);
            } catch (ClassNotFoundException e) {
            }
            if (clazz != null) {
                apiRest.setData(JacksonUtils.readValue(JacksonUtils.writeValueAsString(apiRest.data), clazz, datePattern));
            }
        }
        return apiRest;
    }

    public String toJson() {
        return toJson(Constants.DEFAULT_DATE_PATTERN);
    }

    public String toJson(String datePattern) {
        return JacksonUtils.writeValueAsString(this, datePattern);
    }

    public void sign(String privateKey) {
        sign(privateKey, Constants.DEFAULT_DATE_PATTERN);
    }

    private String concat(String datePattern) {
        Map<String, String> sortedMap = new TreeMap<String, String>();
        sortedMap.put("successful", String.valueOf(successful));
        if (data != null) {
            sortedMap.put("data", JacksonUtils.writeValueAsString(data, datePattern));
        }
        if (StringUtils.isNotBlank(className)) {
            sortedMap.put("className", className);
        }
        if (StringUtils.isNotBlank(message)) {
            sortedMap.put("message", message);
        }
        if (error != null) {
            sortedMap.put("error", JacksonUtils.writeValueAsString(error));
        }
        sortedMap.put("id", id);
        sortedMap.put("timestamp", timestamp);
        sortedMap.put("zipped", String.valueOf(zipped));
        sortedMap.put("encrypted", String.valueOf(encrypted));

        List<String> pairs = new ArrayList<String>();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            if (StringUtils.isBlank(entry.getValue())) {
                continue;
            }
            pairs.add(entry.getKey() + "=" + entry.getValue());
        }
        return StringUtils.join(pairs, "&");
    }

    public void sign(String privateKey, String datePattern) {
        signature = Base64.encodeToString(SignatureUtils.sign(concat(datePattern).getBytes(Constants.CHARSET_UTF_8), Base64.decode(privateKey, Base64.DEFAULT), SignatureUtils.SIGNATURE_TYPE_SHA256_WITH_RSA), Base64.DEFAULT);
    }

    public boolean verifySign(String publicKey, String datePattern) {
        return SignatureUtils.verifySign(concat(datePattern).getBytes(Constants.CHARSET_UTF_8), Base64.decode(publicKey, Base64.DEFAULT), Base64.decode(signature, Base64.DEFAULT), SignatureUtils.SIGNATURE_TYPE_SHA256_WITH_RSA);
    }

    public void zipData() {
        zipData(Constants.DEFAULT_DATE_PATTERN);
    }

    public void zipData(String datePattern) {
        if (data instanceof String) {
            data = ZipUtils.zipText(data.toString());
        } else {
            data = ZipUtils.zipText(JacksonUtils.writeValueAsString(data, datePattern));
        }
        zipped = true;
    }

    public void unzipData() {
        unzipData(Constants.DEFAULT_DATE_PATTERN);
    }

    public void unzipData(String datePattern) {
        data = JacksonUtils.readValue(ZipUtils.unzipText(data.toString()), Object.class, datePattern);
        zipped = false;
    }

    public void encryptData(String publicKey) {
        encryptData(Base64.decode(publicKey, Base64.DEFAULT), Constants.DEFAULT_DATE_PATTERN);
    }

    public void encryptData(String publicKey, String datePattern) {
        encryptData(Base64.decode(publicKey, Base64.DEFAULT), datePattern);
    }

    public void encryptData(byte[] publicKey) {
        encryptData(publicKey, Constants.DEFAULT_DATE_PATTERN);
    }

    public void encryptData(byte[] publicKey, String datePattern) {
        byte[] bytes = null;
        if (data instanceof String) {
            bytes = data.toString().getBytes(Constants.CHARSET_UTF_8);
        } else {
            bytes = JacksonUtils.writeValueAsString(data, datePattern).getBytes(Constants.CHARSET_UTF_8);
        }
        data = Base64.encodeToString(RSAUtils.encryptByPublicKey(bytes, publicKey, RSAUtils.PADDING_MODE_RSA_ECB_PKCS1PADDING), Base64.DEFAULT);
        encrypted = true;
    }

    public void decryptData(String privateKey) {
        decryptData(privateKey, Constants.DEFAULT_DATE_PATTERN);
    }

    public void decryptData(String privateKey, String datePattern) {
        decryptData(Base64.decode(privateKey, Base64.DEFAULT), datePattern);
    }

    public void decryptData(byte[] privateKey) {
        decryptData(privateKey, Constants.DEFAULT_DATE_PATTERN);
    }

    public void decryptData(byte[] privateKey, String datePattern) {
        byte[] bytes = RSAUtils.decryptByPrivateKey(Base64.decode(data.toString(), Base64.DEFAULT), privateKey, RSAUtils.PADDING_MODE_RSA_ECB_PKCS1PADDING);
        String str = new String(bytes, Constants.CHARSET_UTF_8);
        if (zipped) {
            data = str;
        } else {
            data = JacksonUtils.readValue(str, Object.class, datePattern);
        }
        encrypted = false;
    }

    public static class Builder {
        private ApiRest instance = new ApiRest();

        public Builder successful(boolean successful) {
            instance.setSuccessful(successful);
            return this;
        }

        public Builder data(Object data) {
            instance.setData(data);
            return this;
        }

        public Builder className(String className) {
            instance.setClassName(className);
            return this;
        }

        public Builder message(String message) {
            instance.setMessage(message);
            return this;
        }

        public Builder error(Error error) {
            instance.setError(error);
            return this;
        }

        public Builder id(String id) {
            instance.setId(id);
            return this;
        }

        public Builder timestamp(String timestamp) {
            instance.setTimestamp(timestamp);
            return this;
        }

        public Builder timestamp(Date timestamp) {
            instance.setTimestamp(timestamp);
            return this;
        }

        public Builder signature(String signature) {
            instance.setSignature(signature);
            return this;
        }

        public Builder zipped(boolean zipped) {
            instance.setZipped(zipped);
            return this;
        }

        public Builder encrypted(boolean encrypted) {
            instance.setEncrypted(encrypted);
            return this;
        }

        public ApiRest build() {
            ApiRest apiRest = new ApiRest();
            apiRest.setSuccessful(instance.isSuccessful());
            apiRest.setData(instance.getData());
            apiRest.setClassName(instance.getClassName());
            apiRest.setMessage(instance.getMessage());
            apiRest.setError(instance.getError());
            apiRest.setId(instance.getId());
            apiRest.setTimestamp(instance.getTimestamp());
            apiRest.setSignature(instance.getSignature());
            apiRest.setZipped(instance.isZipped());
            apiRest.setEncrypted(instance.isEncrypted());
            return apiRest;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

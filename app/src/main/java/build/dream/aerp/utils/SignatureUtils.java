package build.dream.aerp.utils;

import org.apache.commons.lang3.Validate;

import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.cert.Certificate;

public class SignatureUtils {
    public static final String SIGNATURE_TYPE_SHA1_WITH_RSA = "SHA1WithRSA";
    public static final String SIGNATURE_TYPE_SHA256_WITH_RSA = "SHA256withRSA";

    /**
     * 获取签名器
     *
     * @param signatureType：签名类型
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Signature obtainSignature(String signatureType) throws NoSuchAlgorithmException {
        Validate.isTrue(SIGNATURE_TYPE_SHA1_WITH_RSA.equals(signatureType) || SIGNATURE_TYPE_SHA256_WITH_RSA.equals(signatureType), "不支持的签名方式：" + signatureType + "！");
        return Signature.getInstance(signatureType);
    }

    /**
     * 签名
     *
     * @param data：代签名数据
     * @param privateKey：私钥
     * @param signatureType：签名方式
     * @return
     */
    public static byte[] sign(byte[] data, byte[] privateKey, String signatureType) {
        try {
            Signature signature = obtainSignature(signatureType);
            signature.initSign(RSAUtils.restorePrivateKey(privateKey));
            signature.update(data);
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验证签名
     *
     * @param data：原始数据
     * @param certificate：整数
     * @param sign：签名
     * @param signatureType：签名类型
     * @return
     */
    public static boolean verifySign(byte[] data, Certificate certificate, byte[] sign, String signatureType) {
        try {
            Signature signature = obtainSignature(signatureType);
            signature.initVerify(certificate);
            signature.update(data);
            return signature.verify(sign);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验证签名
     *
     * @param data：原始数据
     * @param publicKey：公钥
     * @param sign：签名
     * @param signatureType：签名方式
     * @return
     */
    public static boolean verifySign(byte[] data, byte[] publicKey, byte[] sign, String signatureType) {
        try {
            Signature signature = obtainSignature(signatureType);
            signature.initVerify(RSAUtils.restorePublicKey(publicKey));
            signature.update(data);
            return signature.verify(sign);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
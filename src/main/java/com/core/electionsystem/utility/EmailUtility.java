package com.core.electionsystem.utility;

import com.core.electionsystem.configuration.security.utility.SecurityUtility;

public final class EmailUtility {

  private EmailUtility() {
    // Default Empty Constructor
  }

  public static final String DEFAULT_MIME_MESSAGE_EMAIL_ENCODING = "UTF-8";
  public static final String DEFAULT_APPLICATION_ADMIN_GMAIL_COM_EMAIL = "orlinivanov69@gmail.com";
  public static final String DEFAULT_APPLICATION_ADMIN_ABV_BG_EMAIL = "orlin11111@abv.bg";
  public static final String SUBJECT_OF_THE_EMAIL_FOR_SECRET_ANSWER_RECOVERY = "Restore Your Secret Answer Access";
  public static final String SUBJECT_OF_THE_EMAIL_FOR_PASSWORD_RECOVERY = "Restore Your Password Access";

  private static final String FORGOTTEN_SECRET_ANSWER_RECOVERY_URL = "http://localhost:5173/reset/secret-answer/token?recoveryToken=";
  private static final String FORGOTTEN_PASSWORD_RECOVERY_URL = "http://localhost:5173/reset/password/token?recoveryToken=";

  private static final String VALIDATED_USER_EMAIL_REPLACE_TARGET = "validatedUserEmail";
  private static final String RECOVERY_TOKEN_URL_REPLACE_TARGET = "recoveryTokenURL";

  public static String buildUrlForSecretAnswerRecovery(char[] recoveryToken) {
    final String stringValueOfRecoveryToken = String.valueOf(recoveryToken);
    SecurityUtility.clearArray(recoveryToken);
    return FORGOTTEN_SECRET_ANSWER_RECOVERY_URL + stringValueOfRecoveryToken;
  }

  public static String buildUrlForPasswordRecovery(char[] recoveryToken) {
    final String stringValueOfRecoveryToken = String.valueOf(recoveryToken);
    SecurityUtility.clearArray(recoveryToken);
    return FORGOTTEN_PASSWORD_RECOVERY_URL + stringValueOfRecoveryToken;
  }

  public static String generateEmailContentForSecretAnswerRecovery(String validatedUserEmail, String recoveryTokenURL) {
    String htmlAndCssContentOfTheEmailForSecretAnswerRecovery = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    line-height: 1.6;
                    color: #333;
                }
                .container {
                    width: 100%;
                    max-width: 600px;
                    margin: 0 auto;
                    padding: 20px;
                    border: 1px solid #ddd;
                    border-radius: 8px;
                    background-color: #f9f9f9;
                }
                .header {
                    text-align: center;
                    padding-bottom: 20px;
                    border-bottom: 1px solid #ddd;
                }
                .header h1 {
                    font-size: 24px;
                    color: #007BFF;
                }
                .content {
                    margin-top: 20px;
                }
                .content p {
                    margin: 10px 0;
                }
                .link-container {
                    margin: 20px 0;
                    padding: 10px;
                    background-color: #f1f1f1;
                    border: 1px solid #ddd;
                    border-radius: 25px;
                    text-align: center;
                }
                .footer {
                    text-align: center;
                    font-size: 12px;
                    color: #666;
                    margin-top: 20px;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>Recovery Token For Secret Answer</h1>
                </div>
                <div class="content">
                    <p>Hello, validatedUserEmail</p>
                    <p>We received a request to help you recover your access to your account and more specifically your Secret Answer property. Please copy and follow the link below and paste it into your browser to proceed with the recovery process of your Secret Answer.</p>
                    <div class="link-container">
                        <p><strong>Your Secret Answer Recovery Link:</strong></p>
                        <p>recoveryTokenURL</p>
                    </div>
                    <p>If you didn’t request this, you can safely ignore and delete this email. Best regards!</p>
                </div>
                <div class="footer">
                    <p>&copy; 2025 Election Express. All rights reserved.</p>
                </div>
            </div>
        </body>
        </html>
        """;
    // @formatter:off
    htmlAndCssContentOfTheEmailForSecretAnswerRecovery = htmlAndCssContentOfTheEmailForSecretAnswerRecovery
        .replace(VALIDATED_USER_EMAIL_REPLACE_TARGET, validatedUserEmail)
        .replace(RECOVERY_TOKEN_URL_REPLACE_TARGET, recoveryTokenURL);
    // @formatter:on
    return htmlAndCssContentOfTheEmailForSecretAnswerRecovery;
  }

  public static String generateEmailContentForPasswordRecovery(String validatedUserEmail, String recoveryTokenURL) {
    String htmlAndCssContentOfTheEmailForPasswordRecovery = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    line-height: 1.6;
                    color: #333;
                }
                .container {
                    width: 100%;
                    max-width: 600px;
                    margin: 0 auto;
                    padding: 20px;
                    border: 1px solid #ddd;
                    border-radius: 8px;
                    background-color: #f9f9f9;
                }
                .header {
                    text-align: center;
                    padding-bottom: 20px;
                    border-bottom: 1px solid #ddd;
                }
                .header h1 {
                    font-size: 24px;
                    color: #007BFF;
                }
                .content {
                    margin-top: 20px;
                }
                .content p {
                    margin: 10px 0;
                }
                .link-container {
                    margin: 20px 0;
                    padding: 10px;
                    background-color: #f1f1f1;
                    border: 1px solid #ddd;
                    border-radius: 25px;
                    text-align: center;
                }
                .footer {
                    text-align: center;
                    font-size: 12px;
                    color: #666;
                    margin-top: 20px;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>Recovery Token For Password</h1>
                </div>
                <div class="content">
                    <p>Hello, validatedUserEmail</p>
                    <p>We received a request to help you recover your access to your account and more specifically your Password property. Please copy and follow the link below and paste it into your browser to proceed with the recovery process of your Password.</p>
                    <div class="link-container">
                        <p><strong>Your Password Recovery Link:</strong></p>
                        <p>recoveryTokenURL</p>
                    </div>
                    <p>If you didn’t request this, you can safely ignore and delete this email. Best regards!</p>
                </div>
                <div class="footer">
                    <p>&copy; 2025 Election Express. All rights reserved.</p>
                </div>
            </div>
        </body>
        </html>
        """;
    // @formatter:off
    htmlAndCssContentOfTheEmailForPasswordRecovery = htmlAndCssContentOfTheEmailForPasswordRecovery
        .replace(VALIDATED_USER_EMAIL_REPLACE_TARGET, validatedUserEmail)
        .replace(RECOVERY_TOKEN_URL_REPLACE_TARGET, recoveryTokenURL);
    // @formatter:on
    return htmlAndCssContentOfTheEmailForPasswordRecovery;
  }
}

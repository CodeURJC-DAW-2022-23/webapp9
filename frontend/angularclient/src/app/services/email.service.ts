import { Injectable } from "@angular/core";
import { createTransport } from "nodemailer";

const transporter = createTransport({
  host: "smtp.gmail.com",
  port: 587,
  auth: {
    user: "tripscannerdaw@gmail.com",
    pass: "aobjtxtkdamimkhy",
  },
});

@Injectable({
  providedIn: "root",
})
export class EmailService {
  constructor() {}

  async sendEmail(to: string) {
    try {
      await transporter.sendMail({
        from: "tripscannerdaw@gmail.com",
        to,
        subject: "Changes in your profile.\n\n",
        text:
          "Dear user of tripscanner, you have recently changed the email associated to your account.\n\n" +
          "This email is just to confirm this change. If you need help or information,\n\n" +
          "please contact us at tripscannerdaw@gmail.com.",
      });

      console.log("Email sent successfully");
    } catch (err) {
      console.error("Failed to send email", err);
    }
  }
}
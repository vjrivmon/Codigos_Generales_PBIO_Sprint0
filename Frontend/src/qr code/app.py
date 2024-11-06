from flask import Flask, request, jsonify, send_file
from flask_cors import CORS
import qrcode
import io

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})  # 允许所有来源请求

# 设置一个示例 PIN
VALID_PIN = "1234"

@app.route('/generate_qr')
def generate_qr():
    mac_address = "092021010403"
    qr = qrcode.make(mac_address)
    buffer = io.BytesIO()
    qr.save(buffer, format="PNG")
    buffer.seek(0)
    return send_file(buffer, mimetype="image/png")

@app.route('/api/associate_device', methods=['POST'])
def associate_device():
    data = request.get_json()
    pin = data.get("pin")

    if pin == VALID_PIN:
        return jsonify({"success": True, "message": "Device associated successfully!"})
    else:
        return jsonify({"success": False, "message": "Invalid PIN"}), 400

if __name__ == '__main__':
    app.run(debug=True)

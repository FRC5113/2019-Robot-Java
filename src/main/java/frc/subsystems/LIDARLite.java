package frc.subsystems;

import java.nio.ByteBuffer;

import edu.wpi.first.wpilibj.I2C.*;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class LIDARLite implements PIDSource {

	private static final byte k_deviceAddress = 0x62;

	private final ByteBuffer m_buffer = ByteBuffer.allocateDirect(2);
	private I2C m_device;

	public LIDARLite(Port port) {
		m_device = new I2C(port, k_deviceAddress);
	}

	public void startMeasuring() {
		writeRegister(0x04, 0x08 | 32); // default plus bit 5
		writeRegister(0x11, 0xff);
		writeRegister(0x00, 0x04);
	}

	public void stopMeasuring() {
		writeRegister(0x11, 0x00);
	}

	public int getDistance() {
		return readShort(0x8f);
	}

	private boolean writeRegister(int address, int value) {
		m_buffer.put(0, (byte) address);
		m_buffer.put(1, (byte) value);

		return m_device.writeBulk(m_buffer, 2);
	}

	private short readShort(int address) {
		short retVal = 0;

		m_buffer.put(0, (byte) address);

		m_device.writeBulk(m_buffer, 1);
		m_device.readOnly(m_buffer, 2);

		retVal = m_buffer.getShort(0);

		if(retVal > 400 || retVal <= 1)
			retVal = 400;

		return retVal;
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		if (pidSource != PIDSourceType.kDisplacement) {
			throw new IllegalArgumentException("Only displacement is supported");
		}
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		return getDistance();
	}
};
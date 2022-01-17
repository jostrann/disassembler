
public class Disassembler {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Calculating...Disassembling... ");
		
		int proCounter = 0x9A040;
		
		int opCode = 0;
		
		int[] instrucArray = new int[] {0x032BA020, 0x8CE90014, 0x12A90003, 0x022DA822, 0xADB30020, 0x02697824, 0xAE8FFFF4,
				0x018C6020, 0x02A4A825, 0x158FFFF7, 0x8ECDFFF0};
		
		for(int i=0; i<instrucArray.length; i++) {		//Disassembling instruction array by for loop
			opCode = (instrucArray[i] & 0xFC000000) >>> 26;
			if(opCode == 0) {
				calculateRFormat(instrucArray[i], proCounter);		//R-format instructions
			}
			else {
				calculateIFormat(instrucArray[i], opCode, proCounter);		//I-format instructions
			}
			proCounter += 0x4;
		}
	}
	
	public static void calculateRFormat(int instruc, int proCounter){
		
		int funct = funct(instruc);
		switch(funct) {
			case 0x20:		//add	
				System.out.println(Integer.toHexString(proCounter) + " add $" + destReg(instruc) + ", $" + srcReg1(instruc) + ", $" + srcReg2(instruc));
				break;
			case 0x22:		//sub	
				System.out.println(Integer.toHexString(proCounter) + " sub $" + destReg(instruc) + ", $" + srcReg1(instruc) + ", $" + srcReg2(instruc));
				break;				
			case 0x24:		//and	
				System.out.println(Integer.toHexString(proCounter) + " and $" + destReg(instruc) + ", $" + srcReg1(instruc) + ", $" + srcReg2(instruc));
				break;
			case 0x25:		//or			
				System.out.println(Integer.toHexString(proCounter) + " or $" + destReg(instruc) + ", $" + srcReg1(instruc) + ", $" + srcReg2(instruc));
				break;
			case 0x2a:		//slt			
				System.out.println(Integer.toHexString(proCounter) + " slt $" + destReg(instruc) + ", $" + srcReg1(instruc) + ", $" + srcReg2(instruc));
				break;
		}
	}
	
	public static void calculateIFormat(int instruc, int opCode, int proCounter){
		short pcro = pcro(instruc);
		if(opCode == 0x4 || opCode == 0x5) {	//beq and bne	
			switch(0x4) {
				case 0x4:	//beq
					System.out.println(Integer.toHexString(proCounter) + " beq $" + srcReg1(instruc) + ", $" + srcReg2(instruc) + ", address " + targetAddress(pcro, proCounter));
					break;
				case 0x5:	//bne
					System.out.println(Integer.toHexString(proCounter) + " bne $" + srcReg1(instruc) + ", $" + srcReg2(instruc) + ", address " + targetAddress(pcro, proCounter));
					break;
			}
		}
		else {
			switch(opCode) {
				case 0x23:	//lw		
					System.out.println(Integer.toHexString(proCounter) + " lw $" + srcReg2(instruc) + ", " + offset(instruc) + "($" + srcReg1(instruc) + ")");
					break;
				case 0x2b:	//sw 	
					System.out.println(Integer.toHexString(proCounter) + " sw $" + srcReg2(instruc) + ", " + offset(instruc) + "($" + srcReg1(instruc) + ")");
					break;
			}
		}
	}
	
	public static int opCode(int instruct) {	//opCode
		int opCode = (instruct & 0xFC000000) >>> 26;
		return opCode;
	}
	
	public static int srcReg1(int instruc) {	//srcReg1
		int srcReg1 = (instruc & 0x03E00000) >>> 21;
		return srcReg1;
	}
	
	public static int srcReg2(int instruc) {	//srcReg2
		int srcReg2 = (instruc & 0x001F0000) >>> 16;
		return srcReg2;
	}
	
	public static int destReg(int instruc) {	//destReg
		int destReg = (instruc & 0x0000F800) >>> 11;
		return destReg;
	}
	
	public static int funct(int instruc) {		//funct
		int funct = (instruc & 0x0000003F);
		return funct;
	}
	
	public static short offset(int instruc) {	//offset
		short offset = (short) (instruc & 0x0000FFFF);
		return offset;
	}
	
	public static short pcro(int instruc) {		//PC-Relative offset
		short pcro = (short) (instruc & 0x0000FFFF);
		return pcro;
	}
	
	public static String targetAddress(short pcro, int proCounter) {	//targetAddresss
		proCounter += 0x4;
		pcro = (short) (pcro << 2);
		int targetAddress = proCounter+pcro;
		return Integer.toHexString(targetAddress);
	}

}

package main;

import java.math.BigInteger;

public class RandomPrimeGenerator {

	public int givePPrime(int max, int min){
		while (true) {
			int pRandom = (int) (Math.random() * (max - min) + min);
			if(isPrime(pRandom)){
				return pRandom;
			}
		}
	}

	public int giveQPrime(int max, int min){
		while (true) {
			int qRandom = (int) (Math.random() * (max - min) + min);
			if(isPrime(qRandom)){
				return qRandom;
			}
		}
	}

	private static boolean isPrime(int n) {
		int i;
		for(i=2;i<=Math.sqrt(n);i++){
			if(n % i == 0){
				return false;
			}
		}
		return true;
	}

	//	private static BigInteger compareRP(BigInteger a, BigInteger b) {
	//		BigInteger t = new BigInteger("0");
	//		while(b != BigInteger.valueOf(0)){
	//			t = a.abs();
	//			a = b.abs();
	//			b = t.mod(b);
	//		}
	//		return a;
	//	}
	public boolean isRelativelyPrime(BigInteger a, BigInteger b) {
		if (a.gcd(b).compareTo(BigInteger.ONE) == 0){
			return true;
		}
		else
			return false;
	}

	public BigInteger GetD(BigInteger e, BigInteger phi){
		BigInteger d = BigInteger.valueOf(1), r; //D = private key, R = remainder
		while(true){
			r = (e.multiply(d).subtract(BigInteger.ONE)).mod(phi);

			if(r == BigInteger.ZERO){
				return d;
			}
			else{d = d.add(BigInteger.ONE);}
		}
	}

	public String getString(byte[] bytes){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<bytes.length;i++){
			byte b = bytes[i];
			sb.append((int)(0x00FF & b));
			if( i+1 <bytes.length ){
				sb.append( "-" );
			}
		}
		return sb.toString();
	}
}
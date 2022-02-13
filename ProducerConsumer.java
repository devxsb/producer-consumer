import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ProducerConsumer {

	Random random = new Random();
	Object lock = new Object();

	private int limit = 10;

	Queue<Integer> queue = new LinkedList<Integer>();

	public void producer() {
		while (true) {

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			synchronized (lock) {

				if (queue.size() == limit) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				Integer value = random.nextInt(100);
				queue.offer(value);
				System.out.println("producer uretiyor: " + value);
				lock.notify();
			}
		}
	}

	public void consumer() {

		while (true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized (lock) {
				if (queue.size() == 0) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Integer value = queue.poll();
				System.out.print("consumer tuketiyor: " + value);
				System.out.println(" queue size: " + queue.size());
				lock.notify();
			}

		}
	}

	public static void main(String[] args) {

		ProducerConsumer pc = new ProducerConsumer();
		Thread producer = new Thread(new Runnable() {

			@Override
			public void run() {
				pc.producer();
			}
		});
		Thread consumer = new Thread(new Runnable() {

			@Override
			public void run() {
				pc.consumer();
			}
		});

		producer.start();
		consumer.start();

		try {
			producer.join();
			consumer.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
}


package example;

import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        int index = 1;
        // 1)
        System.out.println("// "+ index +")");
        index++;
        Flowable.just("Hello world").subscribe(System.out::println);

        // 2)
        System.out.println("// "+ index +")");
        index++;
        Flowable.fromCallable(() -> {
                Thread.sleep(1000); //  imitate expensive computation
                return "Done";
            })
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.single())
            .subscribe(System.out::println, Throwable::printStackTrace);

        Thread.sleep(2000); // <--- wait for the flow to finish

        // 3)
        System.out.println("// "+ index +")");
        index++;
        Flowable<String> source = Flowable.fromCallable(() -> {
                Thread.sleep(1000); //  imitate expensive computation
                return "Done";
            });

        Flowable<String> runBackground = source.subscribeOn(Schedulers.io());
        Flowable<String> showForeground = runBackground.observeOn(Schedulers.single());
        showForeground.subscribe(System.out::println, Throwable::printStackTrace);
        Thread.sleep(2000);

        // 4)
        System.out.println("// "+ index +")");
        index++;
        Flowable.range(1, 10)
            .observeOn(Schedulers.computation())
            .map(v -> v * v)
            .blockingSubscribe(System.out::println);

        // 5)
        System.out.println("// "+ index +")");
        index++;
        Flowable.range(1, 10)
            .flatMap(v ->
                     Flowable.just(v)
                     .subscribeOn(Schedulers.computation())
                     .map(w -> w * w)
                     )
            .blockingSubscribe(System.out::println);

        // 6)
        System.out.println("// "+ index +")");
        index++;
        Flowable.range(1, 10)
            .parallel()
            .runOn(Schedulers.computation())
            .map(v -> v * v)
            .sequential()
            .blockingSubscribe(System.out::println);

        // 7))
        // Flowable<Inventory> inventorySource = warehouse.getInventoryAsync();

        // inventorySource.flatMap(inventoryItem ->
        //                         erp.getDemandAsync(inventoryItem.getId())
        //                         .map(demand
        //                              -> System.out.println("Item " + inventoryItem.getName() + " has demand " + demand))
        //                         )
        //     .subscribe();
    }
}

//
//  Cloud.m
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-03-15.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import "Cloud.h"


static Cloud * sharedInstance = nil;

@implementation Cloud
/*
 Singleton support
 */
+(Cloud *)sharedInstance{
    if(sharedInstance == nil){
        sharedInstance = [[Cloud alloc] init];
    }
    return sharedInstance;
}

/**
 Tells the server about our existance
 **/
-(void)registerDevice{
    
}


/*
    Asks a question
*/
-(void)askQuestion:(Question *)question delegate:(NSObject<AskQuestionDelegate> *)delegate{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    NSDictionary *parameters = @{@"question": @"Will this work?"};
    [manager POST:@"http://google.com" parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [delegate questionAskDidSucceed:question];
        NSLog(@"JSON: %@", responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        //[delegate questionAskDidFail];
        
        [delegate questionAskDidSucceed:question];
        NSLog(@"Error: %@", error);
    }];
}

/*
 Asks the server what they know about us
 */
-(void)getDeviceInfromation:(NSObject<DeviceInformationRequestDelegate> *)delegate{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    NSDictionary *parameters = @{@"question": @"Will this work?"};
    [manager GET:@"http://google.com" parameters:@{@"deviceID":@"sdfjjsdfhkjsdhfjh387283fhh298h"} success:^(AFHTTPRequestOperation * operation, id responseObject){
        // Success
    }
         failure:^(AFHTTPRequestOperation * operation, NSError * error){
             // Failure
         }
     ];
}

/*
 Goes to the server and asks for all of the information that we have not yet seen
 
 For all thread responses, we get the information and pass it back as an array.
 The delegate can worry about putting the information in the data stores and forcing the UI update.
 */
-(void)get{
    
}


@end
